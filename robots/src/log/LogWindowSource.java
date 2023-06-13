package log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он 
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено 
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений 
 * ограниченного размера) 
 */
public class LogWindowSource
{
    private int m_iQueueLength;
    
    private final ArrayList<LogEntry> m_messages;
    private final List<WeakReference<LogChangeListener>> m_listeners;
    private volatile ArrayList<LogChangeListener> m_activeListeners;
    
    public LogWindowSource(int iQueueLength) 
    {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayList<>();
        m_listeners = new ArrayList<>();
    }
    
    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(new WeakReference<>(listener));
        }
    }
    
    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.removeIf(logChangeListenerWeakReference -> logChangeListenerWeakReference.get() == listener);
        }
    }
    
    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        synchronized (m_messages) {
            if (size() < m_iQueueLength){
                m_messages.add(entry);
            } else {
                m_messages.clear();
                Iterable<LogEntry> iterator = range(size() - m_iQueueLength, size());
                iterator.forEach(m_messages::add);
            }
        }

        ArrayList<LogChangeListener> activeListeners = new ArrayList<>();
        synchronized (m_listeners) {
            for (WeakReference<LogChangeListener> reference : m_listeners) {
                LogChangeListener listener = reference.get();
                if (listener != null) {
                    activeListeners.add(listener);
                }
            }
            m_activeListeners = activeListeners;
        }

        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }
    
    public int size()
    {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, size());
        List<LogEntry> subList;
        synchronized (m_messages){
            subList = m_messages.subList(startFrom, indexTo);
        }
        return subList;
    }

    public Iterable<LogEntry> all()
    {
        return range(0, size());
    }
}
