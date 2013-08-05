package net.hizhi.simplecalculator;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	GridLayout 		gridLayout;
	TextView		expressionTextView;
	LinearLayout 	clearDelLinearLayout;
	
	String[] buttonTexts = new String[] 
			{
				"7", "8", "9", "/",
				"4", "5", "6", "x",
				"1", "2", "3", "-",
				".", "0", "=", "+"
			};
	
	private boolean isOperator(char ch) {
		boolean bOperator = false;
		if (ch == '+' || ch == '-' || ch == 'x' || ch == 'X' || ch == '/')
			bOperator = true;
		
		return bOperator;
	}
	
	private boolean appendDotValid(String exp) {
		if (exp.equals("")) 
			return true;
		
		int expLen = exp.length();
		for (int ii = expLen - 1; ii >= 0; --ii) {
			char ch = exp.charAt(ii);
			if (isOperator(ch))
				return true;
			else if (ch == '.')
				return false;
		}
		
		return true;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int	screenWidth = size.x;
        int oneQuarterWidth = (int) (screenWidth * 0.25);
        
		gridLayout = (GridLayout)findViewById(R.id.root);
		
		for (int ii = 0; ii < buttonTexts.length; ii++)
		{
			Button btn = new Button(this);
			btn.setText(buttonTexts[ii]);
			btn.setTextSize(40);
			
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Button bn = (Button)arg0;
					String bnText = bn.getText().toString();
					
					TextView expressionTextView = (TextView)findViewById(R.id.expressionTextView);
					String oldExpression = expressionTextView.getText().toString();
					
					char inputCh = bnText.charAt(bnText.length()-1);
					if (isOperator(inputCh)) {
						if (oldExpression.equals(""))
							return;
						
						char lastCh = oldExpression.charAt(oldExpression.length()-1);
						if (isOperator(lastCh))
							return;
					}
					
					if (inputCh == '.' && !appendDotValid(oldExpression)) {
						return;
					}
					
					String newExpression = null;
					if (bnText.equals("=")) {
						double result = ReversePolishNotation.evalExp(oldExpression);
						newExpression = Double.toString(result);
					}
					else {
						newExpression = oldExpression.concat(bnText);
					}
					
					expressionTextView.setText(newExpression);
				}
			});
			
			GridLayout.Spec rowSpec = GridLayout.spec(ii/4 + 2);
			GridLayout.Spec columnSpec = GridLayout.spec(ii % 4 );
			GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
			params.width = oneQuarterWidth;
			gridLayout.addView(btn, params);
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	public void onClearText(View v) {
		TextView expressionTextView = (TextView)findViewById(R.id.expressionTextView);
		expressionTextView.setText("");
	}
	
	public void onDeleteText(View v) {
		TextView expressionTextView = (TextView)findViewById(R.id.expressionTextView);
		String oldExp = expressionTextView.getText().toString().trim();
		if (oldExp.equals(""))
			return;
		
		// Remove the last character.
		oldExp = oldExp.substring(0, oldExp.length()-1);
		expressionTextView.setText(oldExp);
	}
    
}
