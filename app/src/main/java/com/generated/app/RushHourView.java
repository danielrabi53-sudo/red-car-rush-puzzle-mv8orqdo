package com.generated.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RushHourView extends View {

    public interface OnGameStateChangeListener {
        void onMoveMade(int moveCount);
        void onLevelCompleted(int levelIndex, int moveCount);
    }

    private static final int GRID_SIZE = 6;
    private List<Vehicle> vehicles = new ArrayList<>();
    private Stack<List<Vehicle>> history = new Stack<>();

    private int currentLevel = 0;
    private int movesCount = 0;
    private boolean isWon = false;

    // Keypad navigation cursor
    private int cursorRow = 2;
    private int cursorCol = 0;
    private Vehicle selectedVehicle = null;

    // Paints
    private Paint gridBgPaint;
    private Paint cellPaint;
    private Paint cellStrokePaint;
    private Paint vehiclePaint;
    private Paint carDetailPaint;
    private Paint cursorPaint;
    private Paint selectedPaint;
    private Paint exitPaint;
    private Paint exitSignPaint;

    private RectF boardRect = new RectF();
    private float cellSize = 0f;
    private float boardLeft = 0f;
    private float boardTop = 0f;

    // Touch dragging tracking
    private Vehicle dragVehicle = null;
    private float dragStartX = 0f;
    private float dragStartY = 0f;
    private int vehicleInitialRow = 0;
    private int vehicleInitialCol = 0;
    private boolean touchMovedCar = false;

    private OnGameStateChangeListener stateListener;

    public RushHourView(Context context) {
        super(context);
        init();
    }

    public RushHourView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RushHourView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

        gridBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridBgPaint.setColor(0xFF1E293B);

        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setColor(0xFF0F172A);

        cellStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellStrokePaint.setColor(0xFF334155);
        cellStrokePaint.setStyle(Paint.Style.STROKE);
        cellStrokePaint.setStrokeWidth(2f);

        vehiclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        carDetailPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cursorPaint.setStyle(Paint.Style.STROKE);
        cursorPaint.setStrokeWidth(6f);
        cursorPaint.setColor(0xFFFBBF24); // Amber cursor

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(7f);
        selectedPaint.setColor(0xFF38BDF8); // Bright cyan highlight for active selection

        exitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        exitPaint.setColor(0xFF22C55E);

        exitSignPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        exitSignPaint.setColor(0xFFFFFFFF);
        exitSignPaint.setTextAlign(Paint.Align.CENTER);
        exitSignPaint.setTextSize(26f);
        exitSignPaint.setFakeBoldText(true);

        loadLevel(0);
    }

    public void setOnGameStateChangeListener(OnGameStateChangeListener listener) {
        this.stateListener = listener;
    }

    public void loadLevel(int levelIndex) {
        this.currentLevel = levelIndex;
        List<Vehicle> src = LevelData.getLevel(levelIndex);
        vehicles.clear();
        for (Vehicle v : src) {
            vehicles.add(v.copy());
        }
        history.clear();
        movesCount = 0;
        isWon = false;
        selectedVehicle = null;
        cursorRow = 2;
        cursorCol = 0;

        // Auto select red car for convenience
        for (Vehicle v : vehicles) {
            if (v.isRedCar) {
                cursorRow = v.row;
                cursorCol = v.col;
                break;
            }
        }

        if (stateListener != null) {
            stateListener.onMoveMade(movesCount);
        }
        invalidate();
    }

    public int getMovesCount() {
        return movesCount;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean canUndo() {
        return !history.isEmpty() && !isWon;
    }

    public void undoMove() {
        if (!canUndo()) return;
        List<Vehicle> prev = history.pop();
        vehicles.clear();
        for (Vehicle v : prev) {
            vehicles.add(v.copy());
        }
        if (movesCount > 0) {
            movesCount--;
        }
        selectedVehicle = null;
        if (stateListener != null) {
            stateListener.onMoveMade(movesCount);
        }
        invalidate();
    }

    public void resetLevel() {
        loadLevel(currentLevel);
    }

    private void recordHistoryState() {
        List<Vehicle> snapshot = new ArrayList<>();
        for (Vehicle v : vehicles) {
            snapshot.add(v.copy());
        }
        history.push(snapshot);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float sidePadding = 24f;
        float availableWidth = w - sidePadding * 2;
        float availableHeight = h - sidePadding * 2;

        float boardSide = Math.min(availableWidth, availableHeight);
        cellSize = (boardSide - 12f) / GRID_SIZE;
        boardSide = cellSize * GRID_SIZE + 12f;

        boardLeft = (w - boardSide) / 2f;
        boardTop = (h - boardSide) / 2f;
        boardRect.set(boardLeft, boardTop, boardLeft + boardSide, boardTop + boardSide);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (cellSize <= 0) return;

        // 1. Draw Board Background
        canvas.drawRoundRect(boardRect, 16f, 16f, gridBgPaint);

        // 2. Draw Exit opening notch at row 2 on the right edge
        float exitY1 = boardTop + 6f + LevelData.EXIT_ROW * cellSize + 4f;
        float exitY2 = exitY1 + cellSize - 8f;
        RectF exitRect = new RectF(boardRect.right - 8f, exitY1, boardRect.right + 12f, exitY2);
        canvas.drawRoundRect(exitRect, 6f, 6f, exitPaint);

        // 3. Draw Grid Cells
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                float cellX = boardLeft + 6f + c * cellSize;
                float cellY = boardTop + 6f + r * cellSize;
                RectF cRect = new RectF(cellX + 2f, cellY + 2f, cellX + cellSize - 2f, cellY + cellSize - 2f);
                canvas.drawRoundRect(cRect, 8f, 8f, cellPaint);
                canvas.drawRoundRect(cRect, 8f, 8f, cellStrokePaint);

                // Exit indicator arrow on (row 2, col 5)
                if (r == LevelData.EXIT_ROW && c == LevelData.EXIT_COL) {
                    exitSignPaint.setColor(0x4422C55E);
                    canvas.drawCircle(cellX + cellSize / 2f, cellY + cellSize / 2f, cellSize / 3f, exitSignPaint);
                }
            }
        }

        // 4. Draw Vehicles
        for (Vehicle v : vehicles) {
            drawVehicle(canvas, v);
        }

        // 5. Draw Keypad Cursor & Selected Indicator
        drawCursor(canvas);
    }

    private void drawVehicle(Canvas canvas, Vehicle v) {
        float x1 = boardLeft + 6f + v.col * cellSize + 4f;
        float y1 = boardTop + 6f + v.row * cellSize + 4f;
        float width = (v.isHorizontal ? v.length : 1) * cellSize - 8f;
        float height = (v.isHorizontal ? 1 : v.length) * cellSize - 8f;

        RectF rect = new RectF(x1, y1, x1 + width, y1 + height);

        // Main vehicle body
        vehiclePaint.setColor(v.color);
        canvas.drawRoundRect(rect, 14f, 14f, vehiclePaint);

        // Roof / windshield styling
        if (v.isRedCar) {
            // Distinctive sports car roof for target Red Car
            carDetailPaint.setColor(0xFFFFF1F2);
            float roofW = width * 0.45f;
            float roofH = height * 0.55f;
            float roofX = x1 + (width - roofW) / 2f;
            float roofY = y1 + (height - roofH) / 2f;
            canvas.drawRoundRect(new RectF(roofX, roofY, roofX + roofW, roofY + roofH), 6f, 6f, carDetailPaint);

            // Front headlights (facing right)
            carDetailPaint.setColor(0xFFFDE047);
            canvas.drawCircle(rect.right - 5f, rect.top + 8f, 3.5f, carDetailPaint);
            canvas.drawCircle(rect.right - 5f, rect.bottom - 8f, 3.5f, carDetailPaint);
        } else {
            // Subtle cabin roof for other vehicles
            carDetailPaint.setColor(0x35000000);
            float roofW = v.isHorizontal ? width * 0.6f : width * 0.65f;
            float roofH = v.isHorizontal ? height * 0.65f : height * 0.6f;
            float roofX = x1 + (width - roofW) / 2f;
            float roofY = y1 + (height - roofH) / 2f;
            canvas.drawRoundRect(new RectF(roofX, roofY, roofX + roofW, roofY + roofH), 8f, 8f, carDetailPaint);
        }

        // Selected halo
        if (selectedVehicle == v) {
            canvas.drawRoundRect(rect, 14f, 14f, selectedPaint);
        }
    }

    private void drawCursor(Canvas canvas) {
        float cellX = boardLeft + 6f + cursorCol * cellSize;
        float cellY = boardTop + 6f + cursorRow * cellSize;
        RectF cursorRect = new RectF(cellX + 1f, cellY + 1f, cellX + cellSize - 1f, cellY + cellSize - 1f);

        // Pulsing / solid cursor outline
        canvas.drawRoundRect(cursorRect, 10f, 10f, cursorPaint);
    }

    // Vehicle finder
    private Vehicle findVehicleAt(int r, int c) {
        for (Vehicle v : vehicles) {
            if (v.occupies(r, c)) {
                return v;
            }
        }
        return null;
    }

    // Check if cell is free or belongs to given vehicle
    private boolean isCellFree(int r, int c, Vehicle ignore) {
        if (r < 0 || r >= GRID_SIZE || c < 0 || c >= GRID_SIZE) {
            return false;
        }
        for (Vehicle v : vehicles) {
            if (v != ignore && v.occupies(r, c)) {
                return false;
            }
        }
        return true;
    }

    // Try moving vehicle by steps (+1 or -1)
    private boolean moveVehicle(Vehicle v, int delta) {
        if (isWon || delta == 0) return false;

        int targetDelta = delta > 0 ? 1 : -1;
        boolean movedAny = false;

        while (delta != 0) {
            if (v.isHorizontal) {
                int targetCol = targetDelta > 0 ? v.col + v.length : v.col - 1;
                if (isCellFree(v.row, targetCol, v)) {
                    if (!movedAny) {
                        recordHistoryState();
                    }
                    v.col += targetDelta;
                    movedAny = true;
                    cursorCol = v.col;
                    cursorRow = v.row;
                } else {
                    break;
                }
            } else {
                int targetRow = targetDelta > 0 ? v.row + v.length : v.row - 1;
                if (isCellFree(targetRow, v.col, v)) {
                    if (!movedAny) {
                        recordHistoryState();
                    }
                    v.row += targetDelta;
                    movedAny = true;
                    cursorCol = v.col;
                    cursorRow = v.row;
                } else {
                    break;
                }
            }
            delta -= targetDelta;
        }

        if (movedAny) {
            movesCount++;
            if (stateListener != null) {
                stateListener.onMoveMade(movesCount);
            }
            checkVictory();
            invalidate();
            return true;
        }
        return false;
    }

    private void checkVictory() {
        for (Vehicle v : vehicles) {
            if (v.isRedCar && v.row == LevelData.EXIT_ROW && v.col + v.length >= GRID_SIZE) {
                isWon = true;
                selectedVehicle = null;
                if (stateListener != null) {
                    stateListener.onLevelCompleted(currentLevel, movesCount);
                }
                break;
            }
        }
    }

    // Keypad and D-pad full handling
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isWon) {
            // Pressing enter/5 moves to next level if won
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_5 || keyCode == KeyEvent.KEYCODE_5) {
                loadLevel((currentLevel + 1) % LevelData.TOTAL_LEVELS);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }

        switch (keyCode) {
            // Directional movement: Keypad 2, 4, 6, 8 or D-Pad
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_NUMPAD_2:
            case KeyEvent.KEYCODE_2:
                handleUp();
                return true;

            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_NUMPAD_8:
            case KeyEvent.KEYCODE_8:
                handleDown();
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_NUMPAD_4:
            case KeyEvent.KEYCODE_4:
                handleLeft();
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_NUMPAD_6:
            case KeyEvent.KEYCODE_6:
                handleRight();
                return true;

            // Select / Toggle Vehicle: Keypad 5 or DPAD_CENTER / ENTER
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_5:
            case KeyEvent.KEYCODE_5:
                handleSelectToggle();
                return true;

            // Keypad 1 / 3: Quick cycle between vehicles
            case KeyEvent.KEYCODE_NUMPAD_1:
            case KeyEvent.KEYCODE_1:
                cycleVehicle(-1);
                return true;

            case KeyEvent.KEYCODE_NUMPAD_3:
            case KeyEvent.KEYCODE_3:
                cycleVehicle(1);
                return true;

            // Keypad 7: Undo
            case KeyEvent.KEYCODE_NUMPAD_7:
            case KeyEvent.KEYCODE_7:
                undoMove();
                return true;

            // Keypad 9: Reset
            case KeyEvent.KEYCODE_NUMPAD_9:
            case KeyEvent.KEYCODE_9:
                resetLevel();
                return true;

            // Asterisk: Prev level
            case KeyEvent.KEYCODE_STAR:
                if (currentLevel > 0) {
                    loadLevel(currentLevel - 1);
                } else {
                    loadLevel(LevelData.TOTAL_LEVELS - 1);
                }
                return true;

            // Hash/Pound: Next level
            case KeyEvent.KEYCODE_POUND:
                loadLevel((currentLevel + 1) % LevelData.TOTAL_LEVELS);
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void handleUp() {
        if (selectedVehicle != null) {
            if (!selectedVehicle.isHorizontal) {
                moveVehicle(selectedVehicle, -1);
            }
        } else {
            cursorRow = Math.max(0, cursorRow - 1);
            invalidate();
        }
    }

    private void handleDown() {
        if (selectedVehicle != null) {
            if (!selectedVehicle.isHorizontal) {
                moveVehicle(selectedVehicle, 1);
            }
        } else {
            cursorRow = Math.min(GRID_SIZE - 1, cursorRow + 1);
            invalidate();
        }
    }

    private void handleLeft() {
        if (selectedVehicle != null) {
            if (selectedVehicle.isHorizontal) {
                moveVehicle(selectedVehicle, -1);
            }
        } else {
            cursorCol = Math.max(0, cursorCol - 1);
            invalidate();
        }
    }

    private void handleRight() {
        if (selectedVehicle != null) {
            if (selectedVehicle.isHorizontal) {
                moveVehicle(selectedVehicle, 1);
            }
        } else {
            cursorCol = Math.min(GRID_SIZE - 1, cursorCol + 1);
            invalidate();
        }
    }

    private void handleSelectToggle() {
        if (selectedVehicle != null) {
            // Deselect
            selectedVehicle = null;
        } else {
            // Select vehicle at cursor
            Vehicle atCursor = findVehicleAt(cursorRow, cursorCol);
            if (atCursor != null) {
                selectedVehicle = atCursor;
            }
        }
        invalidate();
    }

    private void cycleVehicle(int direction) {
        if (vehicles.isEmpty()) return;
        int currentIndex = selectedVehicle == null ? -1 : vehicles.indexOf(selectedVehicle);
        int nextIndex = (currentIndex + direction + vehicles.size()) % vehicles.size();
        selectedVehicle = vehicles.get(nextIndex);
        cursorRow = selectedVehicle.row;
        cursorCol = selectedVehicle.col;
        invalidate();
    }

    // Touch support (tap to select, drag to slide)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isWon || cellSize <= 0) return true;

        float x = event.getX();
        float y = event.getY();

        int c = (int) ((x - (boardLeft + 6f)) / cellSize);
        int r = (int) ((y - (boardTop + 6f)) / cellSize);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (c >= 0 && c < GRID_SIZE && r >= 0 && r < GRID_SIZE) {
                    cursorRow = r;
                    cursorCol = c;
                    dragVehicle = findVehicleAt(r, c);
                    selectedVehicle = dragVehicle;
                    if (dragVehicle != null) {
                        dragStartX = x;
                        dragStartY = y;
                        vehicleInitialCol = dragVehicle.col;
                        vehicleInitialRow = dragVehicle.row;
                        touchMovedCar = false;
                    }
                    invalidate();
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (dragVehicle != null) {
                    if (dragVehicle.isHorizontal) {
                        float diffX = x - dragStartX;
                        int cellDelta = Math.round(diffX / cellSize);
                        if (cellDelta != 0) {
                            if (moveVehicle(dragVehicle, cellDelta)) {
                                dragStartX = x;
                                touchMovedCar = true;
                            }
                        }
                    } else {
                        float diffY = y - dragStartY;
                        int cellDelta = Math.round(diffY / cellSize);
                        if (cellDelta != 0) {
                            if (moveVehicle(dragVehicle, cellDelta)) {
                                dragStartY = y;
                                touchMovedCar = true;
                            }
                        }
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragVehicle = null;
                invalidate();
                return true;
        }

        return super.onTouchEvent(event);
    }
}
