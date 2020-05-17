package com.example.newui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 *
 */
public class ShuView extends View {
    Paint paint;
   // PointF[] BPoints;
    PointF ep;//触碰点
    PointF p1,p2;//与屏幕边缘的焦点
    PointF zp1,zp2;//最终与屏幕边缘的焦点
    PointF zxp1,zxp2;//三角形的中心点
    public ShuView(Context context) {
        super(context);
        init();
    }

    public ShuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    Path path;
    private void init() {
        path=new Path();
        paint=new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        //paint.setStrokeJoin(Paint.Join.ROUND);

        paint.setPathEffect(new CornerPathEffect(0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (ep==null) {
            initPointB();
            jiao1=new PointF(getMeasuredWidth(),getMeasuredHeight());
            jiao2=new PointF(getMeasuredWidth(),getMeasuredHeight());
            p1=new PointF(getMeasuredWidth(),getMeasuredHeight());
            p2=new PointF(getMeasuredWidth(),getMeasuredHeight());
            ep=new PointF(getMeasuredWidth(),getMeasuredHeight());
            zp1=new PointF(getMeasuredWidth(),getMeasuredHeight());
            zp2=new PointF(getMeasuredWidth(),getMeasuredHeight());
            zxp1=new PointF(getMeasuredWidth(),getMeasuredHeight());
            zxp2=new PointF(getMeasuredWidth(),getMeasuredHeight());
            O=new PointF(getMeasuredWidth(),getMeasuredHeight());
            jbz2=new PointF(getMeasuredWidth(),getMeasuredHeight());
            jbz1=new PointF(getMeasuredWidth(),getMeasuredHeight());
        }
        canvas.drawColor(Color.BLUE);



        path.reset();
        path.moveTo(ep.x,ep.y);
//       path.quadTo(getMeasuredWidth(),getMeasuredHeight(),p2.x,p2.y);
        path.lineTo(jiao1.x,jiao1.y);
        path.lineTo(jiao2.x,jiao2.y);
        path.lineTo(ep.x,ep.y);

        paint.setColor(Color.YELLOW);
        canvas.drawPath(path,paint);
        path.reset();
        path.moveTo(jiao1.x,jiao1.y);
        PointF quP=getJiao(jiao1,p2,zxp1,zxp2);
        path.quadTo(quP.x,quP.y,zxp1.x,zxp1.y);//todo
        //(1,2) ?:3 ?/x1 =y2/y1*x1

        path.quadTo(jbz1.x,jbz1.y,zp1.x,zp1.y);//todo

       // path.quadTo(zxp1.x,zxp1.y, zp1.x,zp1.y);
        path.lineTo(p2.x,p2.y);
        path.lineTo(p1.x,p1.y);
        path.lineTo(zp2.x,zp2.y);

        path.quadTo(jbz2.x,jbz2.y,zxp2.x,zxp2.y);//todo
        quP=getJiao(jiao2,p1,zxp1,zxp2);
        path.quadTo(quP.x,quP.y,jiao2.x,jiao2.y);//todo
       // path.quadTo(zxp2.x,zxp2.y,jiao2.x,jiao2.y);
        canvas.drawPath(path,paint);

        path.reset();
        path.moveTo(getMeasuredWidth(),getMeasuredHeight());
        path.lineTo(zp1.x,zp1.y);
        path.quadTo(jbz1.x,jbz1.y,zxp1.x,zxp1.y);
        path.lineTo(zxp2.x,zxp2.y);
        path.quadTo(jbz2.x,jbz2.y,zp2.x,zp2.y);
       // path.quadTo(p2.x,p2.y,jiao1.x,jiao1.y);
//        path.lineTo(jiao2.x,jiao2.y);
//        path.quadTo(p1.x,p1.y,zp2.x,zp2.y);
        paint.setColor(Color.RED);
        canvas.drawPath(path,paint);
       // drawC(canvas);
        paint.setColor(Color.BLACK);

    }


    PointF jbz1,jbz2;
    public void getlineFun(PointF pointF1,PointF pointF2){//直线函数 确定一条直线的函数
        //y=kx+b; x=(y-b)/k
        float x1=pointF1.x,y1=pointF1.y,x2=pointF2.x,y2=pointF2.y;
        float k=(y2-y1)/(x2-x1); //k
        float b=y1-(y2-y1)/(x2-x1)*x1;//b

        jbz1.y=getMeasuredHeight();
        jbz1.x=(jbz1.y-b)/k;
        jbz2.x=getMeasuredWidth();
        jbz2.y=k*jbz2.x+b;
    }

    private void initPointB() {
        PointF pointF=new PointF(getMeasuredWidth(),getMeasuredHeight());
//        BPoints=new PointF[]{pointF,pointF,pointF};
        O=pointF;
    }
    //亮点的距离
    public float getSpacingPoint(PointF p1,PointF p2){
        float dx=p2.x-p1.x;
        float dy=p2.y-p1.y;
        return (float) Math.sqrt(dx*dx+dy*dy);//勾股定理
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                changePoint(new PointF(event.getX(),event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                changePoint(new PointF(event.getX(),event.getY()));
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
    PointF O;
    private void changePoint(PointF pointF) {
        //B区域
        ep=pointF;
        //根据X坐标 确定第一个点
        float c=getMeasuredWidth()-pointF.x;
        float d=getMeasuredHeight()-pointF.y;
        float a=(c*c/d+d)/2;
        p1.x=getMeasuredWidth();
        p1.y=getMeasuredHeight()-a;

     //   int d2=getMeasuredHeight();
        float a1=(d*d/c+c)/2;
        p2=new PointF();
        p2.x=getMeasuredWidth()-a1;
          //p1=new PointF(pointF.x,p1.y);
        p2.y=getMeasuredHeight();


        //C区域
        float ca=p2.x-a1/2;
        float cb=p1.y-a/2;

            zp1=new PointF(ca,getMeasuredHeight());
            zp2=new PointF(getMeasuredWidth(),cb);

         jiao1=getJiao(pointF,p2,zp1,zp2);//交点
         jiao2=getJiao(pointF,p1,zp1,zp2);
        //dx=((cx+bx)/2+ex)/2
        zxp1.x=((zp1.x+jiao1.x)/2+p2.x)/2;
        zxp1.y=((zp1.y+jiao1.y)/2+p2.y)/2;

        zxp2.x=((zp2.x+jiao2.x)/2+p1.x)/2;
        zxp2.y=((zp2.y+jiao2.y)/2+p1.y)/2;

        getlineFun(zxp2,zxp1);//计算出焦点 与屏幕边缘
         invalidate();
    }

    PointF jiao1,jiao2;//交点
    public PointF getJiao(PointF pointF1,PointF pointF2, PointF pointF3,PointF pointF4){
        //第一条直线
        double x1 = pointF1.x, y1 = pointF1.y, x2 = pointF2.x, y2 = pointF2.y;
        double a = (y1 - y2) / (x1 - x2);
        double b = (x1 * y2 - x2 * y1) / (x1 - x2);
        //System.out.println("求出该直线方程为: y=" + a + "x + " + b);

        //第二条
        double x3 = pointF3.x, y3 = pointF3.y, x4 = pointF4.x, y4 = pointF4.y;
        double c = (y3 - y4) / (x3 - x4);
        double d = (x3 * y4 - x4 * y3) / (x3 - x4);
   //     System.out.println("求出该直线方程为: y=" + c + "x + " + d);

        double x = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));

        double y = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));

        PointF pointF = new PointF((int)x, (int)y);
        return pointF;
    }

    public PointF getO(PointF pointF1,PointF pointF2,PointF pointF3){//求三角形中心点
        double a1 = pointF1.x;
        double a2 =  pointF2.x;
        double a3 =  pointF3.x;
        double b1 =  pointF1.y;
        double b2 = pointF2.y;
        double b3 = pointF3.y;

        double x = (a1+a2+a3)/3;
        double y = (b1+b2+b3)/3;
       // System.out.println("x: "+x+"  y:  "+y);
        return  new PointF((int)x,(int)y);
    }
}
