package sing.searchlayout.demo;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import sing.widget.SearchLayout;

public class MainActivity extends AppCompatActivity {

    SearchLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchLayout = findViewById(R.id.search_layout);

        searchLayout.setOnSearchListener((str, isSearch) -> Toast.makeText(MainActivity.this, str + "," + isSearch, Toast.LENGTH_SHORT).show());

        searchLayout.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                Toast.makeText(MainActivity.this, "点击了按钮，actionId = " + actionId, Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        searchLayout.setImeOptions(EditorInfo.IME_ACTION_SEND);
    }
}
