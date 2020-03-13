package fr.b.dronegcs_m;

import org.json.JSONException;
import org.json.JSONObject;

public class Instruction {
    private float x,y,z;
    private boolean buttonRightPressed, buttonLeftPressed,buttonTopPressed,buttonBotPressed, isNewPosition;
    private int id;
    private static int idMessage=-1;

    public Instruction(float x, float y, float z, boolean buttonRightPressed, boolean buttonLeftPressed, boolean buttonTopPressed, boolean buttonBotPressed, boolean isNewPosition) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.buttonRightPressed = buttonRightPressed;
        this.buttonLeftPressed = buttonLeftPressed;
        this.buttonTopPressed = buttonTopPressed;
        this.buttonBotPressed = buttonBotPressed;
        this.id = idMessage+1;
        this.isNewPosition = isNewPosition;
    }

    JSONObject getJSON() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id",id);
            obj.put("x",x);
            obj.put("y",y);
            obj.put("z",z);
            obj.put("buttonRightPressed",buttonRightPressed);
            obj.put("buttonLeftPressed",buttonLeftPressed);
            obj.put("buttonTopPressed",buttonTopPressed);
            obj.put("buttonBotPressed",buttonBotPressed);
            obj.put("isNewPosition",isNewPosition);
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

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isButtonRightPressed() {
        return buttonRightPressed;
    }

    public void setButtonRightPressed(boolean buttonRightPressed) {
        this.buttonRightPressed = buttonRightPressed;
    }

    public boolean isButtonLeftPressed() {
        return buttonLeftPressed;
    }

    public void setButtonLeftPressed(boolean buttonLeftPressed) {
        this.buttonLeftPressed = buttonLeftPressed;
    }

    public boolean isButtonTopPressed() {
        return buttonTopPressed;
    }

    public void setButtonTopPressed(boolean buttonTopPressed) {
        this.buttonTopPressed = buttonTopPressed;
    }

    public boolean isButtonBotPressed() {
        return buttonBotPressed;
    }

    public void setButtonBotPressed(boolean buttonBotPressed) {
        this.buttonBotPressed = buttonBotPressed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
