package es.uam.oscar_garcia.othello.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;
import es.uam.oscar_garcia.othello.model.ERBoard;
import logica_juego.MovimientoOthello;


/**
 * Created by oscar on 1/03/17.
 */

public class ERView extends View {

    private final String DEBUG = "ERView";
    private final int VACIA_C=Color.parseColor("#D1C4E9");
    private final int TABLERO_C=Color.parseColor("#9575cd");
    private final int POSIBLE_C=Color.parseColor("#7986CB");
    private  final int JUG1_C=Color.parseColor("#7CB342");
    private final int JUG2_C=Color.parseColor("#FF5722");

    private int numero;
    private Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float heightOfTile;
    private float widthOfTile;
    private float radio;
    private int size=8;
    private ERBoard board;
    OnPlayListener onPlayListener;

    /**
     * Constructor de ERView
     * @param context
     */
    public ERView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor de ERView
     * @param context
     * @param attrs
     */
    public ERView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructor de ERView
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ERView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Constructor de ERView
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ERView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * Inicializa el tablero, y le pone un color
     */
    private void init() {

        this.board=new ERBoard(8);
        backgroundPaint.setColor(TABLERO_C);

        linePaint.setStrokeWidth(2);
    }

    public interface OnPlayListener{
        void onPlay(int i, int j);
    }

    /**
     * Calcula las dimensiones de la pantalla
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 500;
        String wMode, hMode;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthSize < heightSize)
            width = height = heightSize = widthSize;
        else
            width = height = widthSize = heightSize;
        setMeasuredDimension(width, height);
    }

    /**
     * Mira si el tamanyo ha cambiado
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        widthOfTile = w / size;
        heightOfTile = h / size;
        if (widthOfTile < heightOfTile)
            radio = widthOfTile * 0.3f;
        else
            radio = heightOfTile * 0.3f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float boardWidth = getWidth();
        float boardHeight = getHeight();
        canvas.drawRect(0, 0, boardWidth, boardHeight, backgroundPaint);
        drawCircles(canvas, linePaint);
    }

   
        private void drawCircles(Canvas canvas, Paint paint) {
            float centerRaw, centerColumn;
            ArrayList<Movimiento> validos=board.movimientosValidos();
            for (int i = 0; i < size; i++) {
                int pos = size - i - 1;
                centerRaw = heightOfTile * (1 + 2 * pos) / 2f;
                for (int j = 0; j < size; j++) {
                    centerColumn = widthOfTile * (1 + 2 * j) / 2f;
                    setPaintColor(paint, i, j,validos.contains(new MovimientoOthello(i,j)));
                    canvas.drawCircle(centerColumn, centerRaw, radio, paint);
                }
            }
        }

    private void setPaintColor(Paint paint, int i, int j,boolean valido) {
        if(valido)
            paint.setColor(POSIBLE_C);
        else if (board.getTablero(i, j) == ERBoard.JUGADOR1)
            paint.setColor(JUG1_C);
        else if (board.getTablero(i, j) == ERBoard.JUGADOR2)
            paint.setColor(JUG2_C);
        else{
            paint.setColor(VACIA_C);
        }

    }




    public boolean onTouchEvent(MotionEvent event) {
        if (board.getEstado() != Tablero.EN_CURSO)
            return true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onPlayListener.onPlay(fromEventToI(event), fromEventToJ(event));
        }
        return true;
    }
    public void setOnPlayListener(OnPlayListener listener) {
        this.onPlayListener = listener;
    }

    private int fromEventToI(MotionEvent event) {
        int pos = (int) (event.getY() / heightOfTile);
        return size - pos - 1;
    }
    private int fromEventToJ(MotionEvent event) {
        return (int) (event.getX() / widthOfTile);
    }

    public void setBoard(int size, ERBoard board) {
        this.size = size;
        this.board = board;
    }


    }
