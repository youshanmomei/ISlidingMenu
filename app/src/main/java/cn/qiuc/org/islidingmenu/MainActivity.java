package cn.qiuc.org.islidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
        findViewById(R.id.openMenu).setOnClickListener(this);
    }

    public void onMenuClick(View view) {
        //get menu text
        TextView tv_menu = (TextView) view;
        Toast.makeText(this, tv_menu.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openMenu:
                slidingMenu.openMenu();
                break;
            default:
                break;
        }
    }
}
