package client.GameModels;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Highlighter {

    private final XRectangle[] rectangles;
    private final ArrayList<XRectangle> inFront;
    private int selectedIndex;
    private int currentIndex;

    public Highlighter(XRectangle[] rectangles) {
        this.inFront = new ArrayList<>();
        this.selectedIndex = 0;
        this.currentIndex = 0;
        this.rectangles = rectangles;
    }

    public void updateInFront() {
        inFront.clear();
        for (int i = 0; i < rectangles.length; i++) {
            if (rectangles[i].isInFront()) {
                inFront.add(rectangles[i]);
            }
        }
    }

    public void highlight() {
        for (int i = 0; i < inFront.size(); i++) {
            try {
                if (currentIndex % inFront.size() == i) {
                    inFront.get(i).hl(true);
                    selectedIndex = inFront.get(i).getID();
                }
            } catch (FileNotFoundException e) {
                System.out.println("» Exception: " + e.getMessage());
            }
        }

        for (int i = 0; i < 6; i++) {
            try {
                if (i != selectedIndex)
                    rectangles[i].hl(false);
            } catch (FileNotFoundException e) {
                System.out.println("» Exception: " + e.getMessage());
            }
        }
    }

    public void changeHLIndex(boolean increment) {
        currentIndex = increment ? (currentIndex == rectangles.length - 1 ? 0 : currentIndex + 1) : (currentIndex == 0 ? rectangles.length - 1 : currentIndex - 1);
    }

    public int getSelectedFace() {
        return this.selectedIndex;
    }
}
