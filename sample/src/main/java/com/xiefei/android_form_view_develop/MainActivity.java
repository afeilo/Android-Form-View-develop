package com.xiefei.android_form_view_develop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xiefei.mylibrary.DateInterpreter;
import com.xiefei.mylibrary.FormEvent;
import com.xiefei.mylibrary.FormView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements DateInterpreter,FormView.EventClickListener{
    private FormView formView;
    private static String Tag = "MainAty2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        formView = (FormView) findViewById(R.id.weekView);
        List<FormEvent> list = new ArrayList<>();
        FormEvent formEvent1 = new FormEvent();
        formEvent1.setX(1);
        formEvent1.setY(1);
        formEvent1.setText("语文\n 陈宝剑");
        formEvent1.setBackgroundColor(getResources().getColor(R.color.event_color_01));
        list.add(formEvent1);
        FormEvent formEvent2 = new FormEvent();
        formEvent2.setX(2);
        formEvent2.setY(2);
        formEvent2.setText("语文\n 陈宝剑");
        formEvent2.setBackgroundColor(getResources().getColor(R.color.event_color_02));
        list.add(formEvent2);
        formView.setEvents(list);
        formView.setmDateInterpreter(this);
        formView.setmVisibleRowCount(2);
        formView.setEventClickListener(this);
    }
    String [] headerTexts = {"周一","周二","周三","周四","周五","周六","周日"};
    String [] leftTexts = {"一\n二\n节","三\n四\n节","五\n六\n节","七\n八\n节","九\n十\n节","十\n一\n十\n二\n节"};
    @Override
    public String interpretHeaderText(int count) {
        return headerTexts[count];
    }

    @Override
    public String interpretLeftText(int count) {
        return leftTexts[count];
    }

    @Override
    public void onEventClick(FormEvent event) {
        Toast.makeText(this,event.getText(),Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_one_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(1);
                return true;
            case R.id.action_two_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(2);
                return true;
            case R.id.action_three_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(3);
                return true;
            case R.id.action_four_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(4);
                return true;
            case R.id.action_five_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(5);
                return true;
            case R.id.action_six_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(6);
                return true;
            case R.id.action_seven_day_view:
                item.setChecked(true);
                formView.setmVisibleRowCount(7);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
