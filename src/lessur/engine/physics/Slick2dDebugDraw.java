/*
Implementation of JBox2d's DebugDraw using Slick2d
@version 1.0.1
@author liamzebedee

Created by Liam Edwards-Playne
Cryptum Technologies - http://cryptum.net
Licensed under the GPLv3 license

Copyright (C) 2011 by Liam Edwards-Playne
    */


package lessur.engine.physics;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.pooling.arrays.IntArray;
import org.jbox2d.pooling.arrays.Vec2Array;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;

public class Slick2dDebugDraw extends DebugDraw{
    public static int circlePoints = 20;
    Graphics g;
   GameContainer gc;
    /**
     * @param viewport
     */
    public Slick2dDebugDraw(Graphics g,GameContainer gc) {
            super(new OBBViewportTransform());
            viewportTransform.setYFlip(true);
            viewportTransform.setExtents(gc.getWidth()/2, gc.getHeight()/2);
            this.g = g;
          this.gc = gc;
          
            System.out.println("Slick2D DebugDraw for JBox2D has been initialised!");
    }

    private final Vec2Array vec2Array = new Vec2Array();
    /**
     * @see org.jbox2d.callbacks.DebugDraw#drawCircle1(org.jbox2d.common.Vec2, float, org.jbox2d.common.Color3f)
     */
    @Override
    public void drawCircle(Vec2 center, float radius, Color3f color) {
            Vec2[] vecs = vec2Array.get(circlePoints );
            generateCirle(center, radius, vecs, circlePoints);
            drawPolygon(vecs, circlePoints, color);
    }
   
    @Override
    public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor) {
            getWorldToScreenToOut(argPoint, sp1);
            g.setColor(new Color(argColor.x,argColor.y,argColor.z));
           
            sp1.x -= argRadiusOnScreen;
            sp1.y -= argRadiusOnScreen;
            g.fillOval((int)sp1.x, (int)sp1.y, (int)argRadiusOnScreen*2, (int)argRadiusOnScreen*2);
            g.setColor(Color.white);
    }

    private final Vec2 sp1 = new Vec2();
    private final Vec2 sp2 = new Vec2();
    /**
     * @see org.jbox2d.callbacks.DebugDraw#drawSegment(org.jbox2d.common.Vec2, org.jbox2d.common.Vec2, org.jbox2d.common.Color3f)
     */
    @Override
    public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {
            getWorldToScreenToOut(p1, sp1);
            getWorldToScreenToOut(p2, sp2);
            g.setColor(new Color(color.x,color.y,color.z));
           
            g.drawLine((int)sp1.x, (int)sp1.y, (int)sp2.x, (int)sp2.y);
            g.setColor(Color.white);
    }
   
   
    public void drawAABB(AABB argAABB, Color3f color){
       g.setColor(new Color(color.x,color.y,color.z));
            Vec2 vecs[] = vec2Array.get(4);
            argAABB.getVertices(vecs);
            drawPolygon(vecs, 4, color);
            g.setColor(Color.white);
    }
   

    private final Vec2 saxis = new Vec2();
    /**
     * @see org.jbox2d.callbacks.DebugDraw#drawSolidCircle(org.jbox2d.common.Vec2, float, org.jbox2d.common.Vec2, org.jbox2d.common.Color3f)
     */
    @Override
    public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color) {
            Vec2[] vecs = vec2Array.get(circlePoints );
            generateCirle(center, radius, vecs, circlePoints);
            drawSolidPolygon(vecs, circlePoints, color);
            if(axis != null){
                    saxis.set(axis).mulLocal(radius).addLocal(center);
                    drawSegment(center, saxis, color);
            }
            g.setColor(Color.white);
    }

   
    // TODO change IntegerArray to a specific class for int[] arrays
    private final Vec2 temp = new Vec2();
    private final static IntArray xIntsPool = new IntArray();
    private final static IntArray yIntsPool = new IntArray();
    /**
     * @see org.jbox2d.callbacks.DebugDraw#drawSolidPolygon(org.jbox2d.common.Vec2[], int, org.jbox2d.common.Color3f)
     */
    @Override
    public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
           
            int[] xInts = xIntsPool.get(vertexCount);
            int[] yInts = yIntsPool.get(vertexCount);
            Polygon p = new Polygon();
           
           
            for(int i=0; i<vertexCount; i++){
                    getWorldToScreenToOut(vertices[i], temp);
                    xInts[i] = (int)temp.x;
                    yInts[i] = (int)temp.y;
                    p.addPoint(xInts[i], yInts[i]);
            }
           
            g.setColor(new Color(color.x,color.y,color.z));
            g.fill(p); //Draws shape filled with colour
            g.setColor(Color.white);
            //drawPolygon(vertices, vertexCount, color);
    }

    /**
     * @see org.jbox2d.callbacks.DebugDraw#drawString(float, float, java.lang.String, org.jbox2d.common.Color3f)
     */
    @Override
    public void drawString(float x, float y, String s, Color3f color) {
          g.setColor(new Color(color.x,color.y,color.z));
            g.drawString(s, x, y);
            g.setColor(Color.white);
    }
   

    private final Vec2 temp2 = new Vec2();
    /**
     * @see org.jbox2d.callbacks.DebugDraw#drawTransform(org.jbox2d.common.Transform)
     */
    @Override
    public void drawTransform(Transform xf) {         
            getWorldToScreenToOut(xf.position, temp);
            temp2.setZero();
            float k_axisScale = 0.4f;
           
            g.setColor(new Color(1,0,0));
            temp2.x = xf.position.x + k_axisScale * xf.R.col1.x;
            temp2.y = xf.position.y + k_axisScale * xf.R.col1.y;
            getWorldToScreenToOut(temp2, temp2);
            g.drawLine((int)temp.x, (int)temp.y, (int)temp2.x, (int)temp2.y);
           
            g.setColor(new Color(0,1,0));
            temp2.x = xf.position.x + k_axisScale * xf.R.col2.x;
            temp2.y = xf.position.y + k_axisScale * xf.R.col2.y;
            getWorldToScreenToOut(temp2, temp2);
            g.drawLine((int)temp.x, (int)temp.y, (int)temp2.x, (int)temp2.y);
            g.setColor(Color.white);
    }

    // CIRCLE GENERATOR
           
    private void generateCirle(Vec2 argCenter, float argRadius, Vec2[] argPoints, int argNumPoints){
            float inc = MathUtils.TWOPI/argNumPoints;
           
            for(int i=0; i<argNumPoints; i++){
                    argPoints[i].x = (argCenter.x + MathUtils.cos(i*inc)*argRadius);
                    argPoints[i].y = (argCenter.y + MathUtils.sin(i*inc)*argRadius);
            }
            g.setColor(Color.white);
    }

   


}