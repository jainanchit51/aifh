/*
 * Encog(tm) Java Examples v3.3
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2014 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package com.heatonresearch.aifh.examples.classic.som.cluster;

import Jama.Matrix;
import com.heatonresearch.aifh.som.SelfOrganizingMap;

import java.awt.*;

import javax.swing.JPanel;

/**
 *
 * http://www.redblobgames.com/grids/hexagons/
 */
public class HexPanel extends JPanel {
    private SelfOrganizingMap network;
    private int width;
    private int height;
    private int cellSize;
    private boolean displayNumbers = false;

    private double unit;
    private double sq3;
    private double hSpace;
    private double vSpace;

    public HexPanel(SelfOrganizingMap theNetwork, int theCellSize, int theWidth, int theHeight)
    {
        this.network = theNetwork;
        this.width = theWidth;
        this.height = theHeight;
        this.cellSize = theCellSize;

        this.unit = this.cellSize/2.0;
        this.sq3 = Math.sqrt(3.0) * unit;
        this.hSpace = (this.sq3*2)+(this.unit*2.0);
        this.vSpace = this.sq3;
    }

    private int convertColor(double d)
    {
        double result = 128*d;
        result+=128;
        result = Math.min(result, 255);
        result = Math.max(result, 0);
        return (int)result;
    }

    public boolean isDisplayNumbers() {
        return displayNumbers;
    }

    public void setDisplayNumbers(final boolean displayNumbers) {
        this.displayNumbers = displayNumbers;
    }

    private void drawHexagon(Graphics g, Color c, int x,int y) {

        int[] xPoints = new int[7];
        int[] yPoints = new int[7];

        double currentX = x;
        double currentY = y;

        // point 0
        xPoints[0] = (int)currentX;
        yPoints[0] = (int)currentY;

        // point 1
        currentY-=sq3;
        currentX+=unit;
        xPoints[1] = (int)currentX;
        yPoints[1] = (int)currentY;

        // point 2
        currentX+=sq3;
        xPoints[2] = (int)currentX;
        yPoints[2] = (int)currentY;

        // point 3
        currentY=y;
        currentX+=unit;
        xPoints[3] = (int)currentX;
        yPoints[3] = (int)currentY;

        // point 4
        xPoints[4] = xPoints[2];
        yPoints[4] = (int)(y+sq3);

        // point 5
        xPoints[5] = xPoints[1];
        yPoints[5] = (int)(y+sq3);

        // point 6
        xPoints[6] = x;
        yPoints[6] = y;

        g.setColor(c);
        g.fillPolygon(xPoints,yPoints,xPoints.length);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints,yPoints,xPoints.length);
    }

    public void plotCenter(Graphics g, int row, int col) {
        double evenIndent = (this.unit+(this.sq3/2));
        double oddIndent = this.unit*2+this.sq3*1.5;
        double indent = ((row%2==1)?oddIndent:evenIndent);

        int y = (int)(this.sq3+(row * this.sq3 ));
        int x = (int)( (col*((this.unit*2)+(this.sq3*2)))  + indent  );

        g.setColor(Color.BLACK);
        g.drawOval(x,y,2,2);
    }

    @Override
    public void paint(Graphics g)
    {
        FontMetrics fm = g.getFontMetrics();

        int idx = 0;
        boolean indent = false;
        Matrix weights = this.network.getWeights();
        for(int y = 0; y< this.height; y++)
        {
            for(int x = 0; x< this.width; x++)
            {
                int index = (y*this.width)+x;
                int red = convertColor(weights.get(index,0));
                int green = convertColor(weights.get(index,1));
                int blue = convertColor(weights.get(index,2));
                Color c = new Color(red,green,blue);
                int xLoc = (int)((x*hSpace)+(indent?(this.sq3+unit):0));
                int yLoc = (int)((y*vSpace)+this.sq3);
                drawHexagon(g,c,xLoc,yLoc);

                if( isDisplayNumbers() ) {
                    g.setColor(Color.BLACK);
                    g.drawString(""+idx,(int)(xLoc+unit), yLoc);
                    idx++;
                }
            }
            indent = !indent;
        }

        plotCenter(g,2,0);
    }
}
