package com.sumsharp.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sumsharp.lowui.TextField;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.R;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

public class Input extends Activity {
    public static final int INPUT_MODE_ALL = 3;
    public static final int INPUT_MODE_LOWERCHAR = 0;
    public static final int INPUT_MODE_NUMBER = 2;
    public static final int INPUT_MODE_UPPERCHAR = 1;
    public static final char[] LOWER_CHAR = {'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'c', 'v', 'b', 'n', 'm'};
    public static final char[] NUMBER = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    public static final char[] UPPER_CHAR = {'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'C', 'V', 'B', 'N', 'M'};
    public static boolean hasOpened = false;
    /* access modifiers changed from: private */
    public static int inputMode = 0;
    /* access modifiers changed from: private */
    public static int length;
    /* access modifiers changed from: private */
    public static CommandListener listener;
    private static String text;
    private static String title;
    Button cancel;
    EditText et;
    Button send;
    TextView tip;
    TextView tv;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);
        this.tv = (TextView) findViewById(R.id.input_title);
        this.tv.setText(title);
        this.et = (EditText) findViewById(R.id.input_edit);
        this.et.setText(text);
        this.et.setMinWidth(length * 16);
        this.et.setTransformationMethod(null);
        this.tip = (TextView) findViewById(R.id.input_tip);
        this.et.addTextChangedListener(new TextWatcher() {
            private boolean isEdit = true;
            private CharSequence temp;

            public void afterTextChanged(Editable s) {
                if (this.temp.length() > Input.length) {
                    this.isEdit = false;
                    s.delete(this.temp.length() - 1, this.temp.length());
                    Input.this.et.setText(s);
                }
                String letter = "字符";
                if (Input.inputMode == 2) {
                    letter = "数字";
                }
                Input.this.tip.setText("(还可输入" + (Input.length - s.length()) + "个" + letter + ")");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.temp = s;
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!this.isEdit) {
                    Editable etext = Input.this.et.getText();
                    Selection.setSelection(etext, etext.length());
                }
            }
        });
        if (inputMode == 2) {
            this.et.setInputType(2);
        }
        this.send = (Button) findViewById(R.id.input_send);
        this.send.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Input.listener.commandAction(new Command(Input.this.et.getText().toString(), 4, 0), null);
                Input.this.close();
            }
        });
        this.cancel = (Button) findViewById(R.id.input_cancel);
        this.cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Input.listener != null) {
                    Input.listener.commandAction(new Command("返回", 2, 0), null);
                    Input.this.close();
                }
            }
        });
    }

    public static void init(CommandListener listen, String t, String value, int lenth, int mode) {
        inputMode = mode;
        if (mode != 2) {
            inputMode = 3;
        }
        listener = listen;
        title = t;
        text = value;
        length = lenth;
    }

    public static void open() {
        try {
            Activity active = MonsterMIDlet.instance;
            Intent i = new Intent();
            i.setClass(active, Input.class);
            active.startActivity(i);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void close() {
        if (listener instanceof TextField) {
            ((TextField) listener).showKeypad = false;
        }
        listener = null;
        title = "";
        text = "";
        finish();
        hasOpened = false;
    }
}
