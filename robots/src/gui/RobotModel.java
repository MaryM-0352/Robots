package gui;

import MathUtils.MathUtils;

import java.awt.*;
import java.util.Observable;

public class RobotModel extends Observable {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private double currentX;
    private double currentY;
    protected void onModelUpdateEvent()
    {
        double distance = MathUtils.distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5 * 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = MathUtils.angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        if (Math.abs(angleToTarget - m_robotDirection) > Math.PI){
            angularVelocity = -angularVelocity;
        }
        moveRobot(velocity, angularVelocity, 10);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = MathUtils.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = MathUtils.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = MathUtils.asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
        setCurrentPosition(m_robotPositionX, m_robotPositionY);
    }

    public void setCurrentPosition(double robotPositionX, double robotPositionY){
        currentX = robotPositionX;
        currentY = robotPositionY;
        setChanged();
        notifyObservers();
        clearChanged();
    }
    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    public double getCurrentX(){
        return currentX;
    }
    public double getCurrentY(){
        return currentY;
    }
    public double getRobotPositionX() {return  m_robotPositionX; }
    public double getRobotPositionY() { return m_robotPositionY; }
    public int getTargetPositionX() { return m_targetPositionX; }
    public int getTargetPositionY() { return m_targetPositionY; }
    public double getRobotDirection() { return m_robotDirection; }
}
