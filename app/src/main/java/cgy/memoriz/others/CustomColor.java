package cgy.memoriz.others;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ProgressBar;

public class CustomColor extends ProgressDialog {

    private int progressColor;

    public CustomColor(Context context, int color) {
        super(context);
        this.progressColor = color;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressBar progress = (ProgressBar) findViewById(android.R.id.progress);
        progress.getIndeterminateDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
    }
}
