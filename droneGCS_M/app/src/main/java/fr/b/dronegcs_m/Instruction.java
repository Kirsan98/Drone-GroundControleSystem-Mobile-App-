package fr.b.dronegcs_m;

import org.json.JSONException;
import org.json.JSONObject;

public class Instruction {
    private float x,y,z, xSpeed, ySpeed, zSpeed;
    private boolean toLand, toTakeOff, isNewPosition;
    private int id;
    private static int idMessage=-1;

    public Instruction(float x, float y, float z, boolean isNewPosition, float xSpeed, float ySpeed, float zSpeed, boolean toLand, boolean toTakeOff) {
        this.id = idMessage+1;
        this.x = x;
        this.y = y;
        this.z = z;
        this.isNewPosition = isNewPosition;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        this.toLand = toLand;
        this.toTakeOff = toTakeOff;
    }

    JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",id);
            obj.put("x",x);
            obj.put("y",y);
            obj.put("z",z);
            obj.put("isNewPosition",isNewPosition);
            obj.put("xSpeed",xSpeed);
            obj.put("ySpeed",ySpeed);
            obj.put("zSpeed",zSpeed);
            obj.put("toLand",toLand);
            obj.put("toTakeOff",toTakeOff);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public boolean isNewPosition() {
        return isNewPosition;
    }

    public void setNewPosition(boolean newPosition) {
        isNewPosition = newPosition;
    }

    public static int getIdMessage() {
        return idMessage;
    }

    public static void setIdMessage(int idMessage) {
        Instruction.idMessage = idMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getzSpeed() {
        return zSpeed;
    }

    public void setzSpeed(float zSpeed) {
        this.zSpeed = zSpeed;
    }

    public boolean isToLand() {
        return toLand;
    }

    public void setToLand(boolean toLand) {
        this.toLand = toLand;
    }

    public boolean isToTakeOff() {
        return toTakeOff;
    }

    public void setToTakeOff(boolean toTakeOff) {
        this.toTakeOff = toTakeOff;
    }
}
