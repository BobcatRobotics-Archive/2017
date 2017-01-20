package org.usfirst.frc.team177.robot;

public enum MotorEnum {
    LeftRear(0), 
    LeftMiddle(1), 
    LeftFront(2), 
    RightRear(3),
    RightMiddle(4),
 	RightFront(5);

    public final int value;

    private MotorEnum(int value) {
      this.value = value;
    }

}
