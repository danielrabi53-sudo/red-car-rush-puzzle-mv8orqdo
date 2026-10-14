package com.generated.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String PREFS_NAME = "rush_hour_prefs";
    private static final String KEY_BEST_PREFIX = "best_level_";

    private RushHourView rushHourView;
    private TextView tvLevelName;
    private TextView tvMovesCount;
    private TextView tvBestScore;

    private Button btnPrev;
    private Button btnNext;
    private Button btnUndo;
    private Button btnReset;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        rushHourView = (RushHourView) findViewById(R.id.rush_hour_view);
        tvLevelName = (TextView) findViewById(R.id.tv_level_name);
        tvMovesCount = (TextView) findViewById(R.id.tv_moves_count);
        tvBestScore = (TextView) findViewById(R.id.tv_best_score);

        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnUndo = (Button) findViewById(R.id.btn_undo);
        btnReset = (Button) findViewById(R.id.btn_reset);

        setupListeners();
        updateHeader(rushHourView.getCurrentLevel(), 0);

        // Keep view focused for direct keypad responsiveness
        rushHourView.requestFocus();
    }

    private void setupListeners() {
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prev = (rushHourView.getCurrentLevel() - 1 + LevelData.TOTAL_LEVELS) % LevelData.TOTAL_LEVELS;
                rushHourView.loadLevel(prev);
                updateHeader(prev, 0);
                rushHourView.requestFocus();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int next = (rushHourView.getCurrentLevel() + 1) % LevelData.TOTAL_LEVELS;
                rushHourView.loadLevel(next);
                updateHeader(next, 0);
                rushHourView.requestFocus();
            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rushHourView.undoMove();
                rushHourView.requestFocus();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rushHourView.resetLevel();
                rushHourView.requestFocus();
            }
        });

        rushHourView.setOnGameStateChangeListener(new RushHourView.OnGameStateChangeListener() {
            @Override
            public void onMoveMade(int moveCount) {
                updateHeader(rushHourView.getCurrentLevel(), moveCount);
            }

            @Override
            public void onLevelCompleted(int levelIndex, int moveCount) {
                handleVictory(levelIndex, moveCount);
            }
        });
    }

    private void updateHeader(int levelIndex, int moves) {
        tvLevelName.setText(getString(R.string.level_label, levelIndex + 1, LevelData.TOTAL_LEVELS));
        tvMovesCount.setText(getString(R.string.moves_label, moves));

        int best = prefs.getInt(KEY_BEST_PREFIX + levelIndex, -1);
        if (best > 0) {
            tvBestScore.setText(getString(R.string.best_label, String.valueOf(best)));
        } else {
            tvBestScore.setText(getString(R.string.best_label, "-"));
        }
    }

    private void handleVictory(final int levelIndex, final int moveCount) {
        // Save best score
        int best = prefs.getInt(KEY_BEST_PREFIX + levelIndex, -1);
        if (best == -1 || moveCount < best) {
            prefs.edit().putInt(KEY_BEST_PREFIX + levelIndex, moveCount).apply();
        }
        updateHeader(levelIndex, moveCount);

        // Show victory dialog
        new AlertDialog.Builder(this)
                .setTitle(R.string.win_title)
                .setMessage(getString(R.string.win_message, levelIndex + 1, moveCount))
                .setCancelable(false)
                .setPositiveButton(R.string.btn_next_level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int next = (levelIndex + 1) % LevelData.TOTAL_LEVELS;
                        rushHourView.loadLevel(next);
                        updateHeader(next, 0);
                        rushHourView.requestFocus();
                    }
                })
                .setNegativeButton(R.string.btn_replay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rushHourView.resetLevel();
                        rushHourView.requestFocus();
                    }
                })
                .show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Forward game keys directly to the RushHourView if it doesn't currently hold focus
        int code = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (code == KeyEvent.KEYCODE_DPAD_UP ||
                code == KeyEvent.KEYCODE_DPAD_DOWN ||
                code == KeyEvent.KEYCODE_DPAD_LEFT ||
                code == KeyEvent.KEYCODE_DPAD_RIGHT ||
                code == KeyEvent.KEYCODE_DPAD_CENTER ||
                code == KeyEvent.KEYCODE_ENTER ||
                code == KeyEvent.KEYCODE_NUMPAD_5 ||
                code == KeyEvent.KEYCODE_5 ||
                code == KeyEvent.KEYCODE_NUMPAD_2 ||
                code == KeyEvent.KEYCODE_2 ||
                code == KeyEvent.KEYCODE_NUMPAD_4 ||
                code == KeyEvent.KEYCODE_4 ||
                code == KeyEvent.KEYCODE_NUMPAD_6 ||
                code == KeyEvent.KEYCODE_6 ||
                code == KeyEvent.KEYCODE_NUMPAD_8 ||
                code == KeyEvent.KEYCODE_8 ||
                code == KeyEvent.KEYCODE_NUMPAD_1 ||
                code == KeyEvent.KEYCODE_1 ||
                code == KeyEvent.KEYCODE_NUMPAD_3 ||
                code == KeyEvent.KEYCODE_3 ||
                code == KeyEvent.KEYCODE_NUMPAD_7 ||
                code == KeyEvent.KEYCODE_7 ||
                code == KeyEvent.KEYCODE_NUMPAD_9 ||
                code == KeyEvent.KEYCODE_9 ||
                code == KeyEvent.KEYCODE_STAR ||
                code == KeyEvent.KEYCODE_POUND) {

                if (!rushHourView.isFocused()) {
                    rushHourView.requestFocus();
                }
                return rushHourView.onKeyDown(code, event);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rushHourView.requestFocus();
    }
}
