package com.sumsharp.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.sumsharp.monster.MonsterMIDlet;
import com.sumsharp.monster.R;
import com.sumsharp.monster.World;
import com.sumsharp.monster.common.CommonComponent;
import com.sumsharp.monster.common.CommonData;
import com.sumsharp.monster.common.GlobalVar;
import com.sumsharp.monster.common.Utilities;
import com.sumsharp.monster.common.data.Chat;
import com.sumsharp.monster.net.UWAPSegment;
import java.io.IOException;
import java.util.Vector;

public class ChatUI extends Activity implements OnClickListener {
    public static final int CONTENT_MAX = 40;
    public static boolean form = false;
    public static ChatUI instance;
    public static Vector tarList = new Vector();
    private Button addEmote;
    private Button addItem;
    /* access modifiers changed from: private */
    public TextView chatTip;
    private RadioGroup chlChoice;
    private Button close;
    /* access modifiers changed from: private */
    public EditText content;
    public String contentTitle = "消息内容：";
    private int lastChannel = -2;
    private byte[] lock = new byte[0];
    private Button send;
    public String tarName = null;
    private AutoCompleteTextView target;
    private int type;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        this.send = (Button) findViewById(R.id.send);
        this.send.setOnClickListener(this);
        this.close = (Button) findViewById(R.id.cancel);
        this.close.setOnClickListener(this);
        this.addEmote = (Button) findViewById(R.id.emotion);
        this.addEmote.setOnClickListener(this);
        this.addItem = (Button) findViewById(R.id.equip);
        this.addItem.setOnClickListener(this);
        this.content = (EditText) findViewById(R.id.chat_edit);
        this.chatTip = (TextView) findViewById(R.id.chat_tip);
        this.content.addTextChangedListener(new TextWatcher() {
            private boolean isEdit = true;
            private CharSequence temp;

            public void afterTextChanged(Editable s) {
                if (this.temp.length() > 40) {
                    this.isEdit = false;
                    s.delete(this.temp.length() - 1, this.temp.length());
                    ChatUI.this.content.setText(s);
                }
                if (s.length() > 0) {
                    ChatUI.this.chatTip.setText("(还可输入" + (40 - s.length()) + "个字符)");
                    return;
                }
                ChatUI.this.chatTip.setText("(不允许发送空消息)");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                this.temp = s;
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!this.isEdit) {
                    Editable etext = ChatUI.this.content.getText();
                    Selection.setSelection(etext, etext.length());
                }
            }
        });
        this.content.setHint("输入聊天信息(限40字)");
        this.chlChoice = (RadioGroup) findViewById(R.id.radio);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 17367050, tarList);
        this.target = (AutoCompleteTextView) findViewById(R.id.edit_private);
        this.target.setAdapter(adapter);
        this.target.setHint("输入要发送的名字");
        this.chatTip = (TextView) findViewById(R.id.chat_tip);
        config();
    }

    public static void open() {
        try {
            Activity active = MonsterMIDlet.instance;
            Intent i = new Intent();
            i.setClass(active, ChatUI.class);
            active.startActivity(i);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void setTitle(String title, int type2) {
        setTitle(title, "", type2);
    }

    public void setTitle(String title, String tip, int type2) {
        setTitle(title);
        this.chatTip.setText(tip);
        this.type = type2;
        switch (type2) {
            case Chat.CHANNEL_TEAM /*-5*/:
                this.chlChoice.check(R.id.radio_team);
                return;
            case Chat.CHANNEL_GUILD /*-3*/:
                this.chlChoice.check(R.id.radio_guild);
                return;
            case Chat.CHANNEL_MAP /*-2*/:
                this.chlChoice.check(R.id.radio_local);
                return;
            case -1:
                this.chlChoice.check(R.id.radio_world);
                return;
            default:
                return;
        }
    }

    public void setTip(String tip) {
        this.chatTip.setText(tip);
    }

    public void setTarPlayer(String name) {
        if (!name.equals("")) {
            this.tarName = new String(name);
            this.target.setText(this.tarName);
        }
    }

    public void changeContentTitle(String string) {
    }

    public void setContent(String str) {
        this.content.setText(str);
    }

    private void config() {
        synchronized (this.lock) {
            if (instance != null) {
                setTitle(instance.getTitle());
                this.chatTip.setText(instance.chatTip.getText().toString());
                if (instance.tarName != null) {
                    this.target.setText(instance.tarName);
                }
                this.content.setText(instance.content.getText().toString().trim());
                setChatChannel();
            }
            instance = this;
            if (!form) {
                form = true;
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.equip /*2131034114*/:
                GlobalVar.setGlobalValue((String) "CHAT_CHOOSE_TYPE", 1);
                GlobalVar.setGlobalValue((String) "CHAT_CHOOSE", (Object) "");
                CommonComponent.loadUI("ui_chatChoose");
                CommonData.player.orderBag();
                World.fullChatChoose = true;
                finish();
                return;
            case R.id.emotion /*2131034115*/:
                GlobalVar.setGlobalValue((String) "CHAT_CHOOSE_TYPE", 0);
                GlobalVar.setGlobalValue((String) "CHAT_CHOOSE", (Object) "");
                CommonComponent.loadUI("ui_chatChoose");
                World.fullChatChoose = true;
                finish();
                return;
            case R.id.send /*2131034116*/:
                if (send()) {
                    close();
                    return;
                }
                return;
            case R.id.cancel /*2131034117*/:
                close();
                return;
            default:
                return;
        }
    }

    private void close() {
        setTip("");
        this.tarName = null;
        form = false;
        CommonData.player.setActionState(0);
        finish();
    }

    private boolean send() {
        CommonComponent.closeMessage();
        String msg = this.content.getText().toString();
        if (msg.trim().length() == 0) {
            return false;
        }
        String tarString = this.target.getText().toString().trim();
        if (this.target != null && !tarString.equals("")) {
            msg = "/" + tarString + " " + msg;
        }
        int chl = -2;
        if (this.type == 0) {
            chl = getChannel(this.chlChoice.getCheckedRadioButtonId());
        } else if (this.type == 1) {
            chl = -2;
        } else if (this.type == 2) {
            chl = -1;
        } else if (this.type == 3) {
            chl = -5;
        } else if (this.type == 4) {
            chl = -3;
        }
        UWAPSegment segment = new UWAPSegment(19, 1);
        try {
            segment.writeInt(0);
            segment.writeString("");
            segment.writeInt(chl);
            segment.writeString("");
            segment.writeString(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utilities.sendRequest(segment);
        this.lastChannel = chl;
        return true;
    }

    public int getChannel(int id) {
        if (id == R.id.radio_world) {
            return -1;
        }
        if (id == R.id.radio_local) {
            return -2;
        }
        if (id == R.id.radio_team) {
            return -5;
        }
        return id == R.id.radio_guild ? -3 : -2;
    }

    public void setChatChannel(int chl) {
        this.lastChannel = chl;
        setChatChannel();
    }

    public void setChatChannel() {
        if (instance != null) {
            this.lastChannel = instance.lastChannel;
        }
        if (this.lastChannel == -8) {
            String tarName2 = CommonData.player.whisperName;
            if (tarName2 != null && !tarName2.equals("")) {
                this.target.setText(tarName2);
            }
        }
        if (this.lastChannel != -8) {
            switch (this.lastChannel) {
                case Chat.CHANNEL_TEAM /*-5*/:
                    this.chlChoice.check(R.id.radio_team);
                    return;
                case Chat.CHANNEL_GUILD /*-3*/:
                    this.chlChoice.check(R.id.radio_guild);
                    return;
                case Chat.CHANNEL_MAP /*-2*/:
                    this.chlChoice.check(R.id.radio_local);
                    return;
                case -1:
                    this.chlChoice.check(R.id.radio_world);
                    return;
                default:
                    return;
            }
        }
    }

    public void insertString(String str) {
        this.content.setText(String.valueOf(this.content.getText().toString()) + str);
        if (this.content.getText().toString().trim().equals("")) {
            this.chatTip.setText(String.valueOf(this.contentTitle) + "(不允许发送空消息)");
            return;
        }
        this.chatTip.setText(String.valueOf(this.contentTitle) + "(还可输入" + (this.content.length() - this.content.getText().length()) + "个字)");
    }
}
