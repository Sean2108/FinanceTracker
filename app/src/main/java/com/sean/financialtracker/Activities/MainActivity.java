package com.sean.financialtracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.sean.financialtracker.Data.DBHandler;
import com.sean.financialtracker.Data.DBHandlerImpl;
import com.sean.financialtracker.Data.Expenditure;
import com.sean.financialtracker.Fragments.FragmentAdapter;
import com.sean.financialtracker.R;
import com.sean.financialtracker.Utils.BudgetDialog;
import com.sean.financialtracker.Utils.DecimalDigitsInputFilter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Field;
import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    private DBHandler db;
    private static final String PREFS_NAME = "MyBudget";
    private Activity thisActivity = this;
    private ViewPager viewPager;
    private SlidingUpPanelLayout slider;
    private ShowcaseView showcase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((EditText)findViewById(R.id.exp_text)).setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});

        db = new DBHandlerImpl(this);

        slider = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
        if (!(settings.contains("daily_budget") || settings.contains("weekly_budget") || settings.contains("monthly_budget"))) {
            showHelp();
        }

        viewPager = (ViewPager)findViewById(R.id.vpPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

        if (getIntent().getExtras() != null) viewPager.setCurrentItem(getIntent().getIntExtra("currentView", 0));
        else viewPager.setCurrentItem(0);

        Button dailyToggle = (Button) findViewById(R.id.daily_toggle);
        Button weeklyToggle = (Button) findViewById(R.id.weekly_toggle);
        Button monthlyToggle = (Button) findViewById(R.id.monthly_toggle);

        final Button[] buttonArr = {dailyToggle, weeklyToggle, monthlyToggle};
        buttonArr[viewPager.getCurrentItem()].setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {

                for (Button b : buttonArr) b.setTextColor(Color.BLACK);
                buttonArr[position % 3].setTextColor(ContextCompat.getColor(thisActivity, R.color.colorPrimary));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final ImageButton popupMenu = (ImageButton) findViewById(R.id.popupmenu);
        popupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, popupMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.actions, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitleCondensed().equals("help")) showHelp();
                        else {
                            Intent intent = new Intent(thisActivity, ListExpActivity.class);
                            intent.putExtra("EXP_TYPE", item.getTitleCondensed());
                            startActivity(intent);
                        }
                        return true;
                    }
                });

                try {
                    Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
                    mFieldPopup.setAccessible(true);
                    MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                    mPopup.setForceShowIcon(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.show(); //showing popup menu
            }
        });
    }

    public void onClickNewExp(View v) {
        try {
            float exp_cost = Float.parseFloat(((EditText) findViewById(R.id.exp_text)).getText().toString());
            String desc = ((EditText) findViewById(R.id.exp_desc)).getText().toString();
            int checkedButtonId = ((RadioGroup) findViewById(R.id.exp_type)).getCheckedRadioButtonId();
            if (checkedButtonId == -1) {
                Toast.makeText(this, "Category is not selected!", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedTypeButton = (RadioButton) findViewById(checkedButtonId);
            String selectedType = selectedTypeButton.getText().toString();

            String date = new Timestamp(System.currentTimeMillis()).toString();

            if (exp_cost == 0f) Toast.makeText(this, "Expenditure cannot be zero!", Toast.LENGTH_SHORT).show();
            else if (desc.equals("")) Toast.makeText(this, "Description cannot be empty!", Toast.LENGTH_SHORT).show();
            else if (desc.equals("")) Toast.makeText(this, "Description cannot be empty!", Toast.LENGTH_SHORT).show();
            else {
                Expenditure exp = new Expenditure(1, exp_cost, selectedType, desc, date);
                db.addExp(exp);
                Toast.makeText(this, "Expenditure added", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Expenditure cannot be empty!", Toast.LENGTH_SHORT).show();
        }

    }

    public void changeBudget(View v) {
        createBudgetDialog();
    }

    public void createBudgetDialog() {
        BudgetDialog budgetDialog = new BudgetDialog(getSharedPreferences(PREFS_NAME, 0), this);
        budgetDialog.buildDialog();
    }

    public void onClickChangeTab(View v) {
        switch (v.getId()) {
            case R.id.daily_toggle:
                viewPager.setCurrentItem(0);
                break;
            case R.id.weekly_toggle:
                viewPager.setCurrentItem(1);
                break;
            case R.id.monthly_toggle:
                viewPager.setCurrentItem(2);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        intent.putExtra("currentView", viewPager.getCurrentItem());
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (slider.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else if (showcase != null) {
            showcase.hide();
            showcase = null;
        }
        else super.onBackPressed();
    }

    public void showHelp() {
        showcase = new ShowcaseView.Builder(this, true)
                .setTarget(new ViewTarget(R.id.vpPager, this))
                .setContentTitle("Set budgets")
                .setContentText("Tap the centre of the chart to set your daily, weekly and monthly budgets.")
                .setStyle(R.style.CustomShowcaseTheme)
                .hideOnTouchOutside()
                .build();
        showcase.setButtonText("Next");

        showcase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        showcase.setTarget(new ViewTarget(R.id.toggle_parent, thisActivity));
                        showcase.setContentTitle("Select Time Range");
                        showcase.setContentText("Tap the buttons or swipe the screen to switch between daily, weekly and monthly expense charts.");
                        break;
                    case 2:
                        showcase.setTarget(new ViewTarget(R.id.dragView, thisActivity));
                        showcase.setContentTitle("Add an expense");
                        showcase.setContentText("Tap or drag the bottom bar upwards to input a new expense.");
                        break;
                    case 3:
                        try {
                            slider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            Thread.sleep(200);
                            showcase.setTarget(new ViewTarget(R.id.exp_fields, thisActivity));
                            showcase.setContentTitle("Input the expense amount");
                            showcase.setContentText("How much did you spend?");
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    case 4:
                        showcase.setTarget(new ViewTarget(R.id.desc_fields, thisActivity));
                        showcase.setContentTitle("Input the expense description");
                        showcase.setContentText("What did you pay for?");
                        break;
                    case 5:
                        showcase.setTarget(new ViewTarget(R.id.category_fields, thisActivity));
                        showcase.setContentTitle("Select the expense category");
                        showcase.setContentText("Which category does this expense belong to?");
                        break;
                    case 6:
                        showcase.setTarget(new ViewTarget(R.id.exp_type, thisActivity));
                        showcase.setContentTitle("Categories");
                        showcase.setContentText("From left: Food, Transport, Entertainment, Subscriptions and Others.");
                        break;
                    case 7:
                        try {
                            slider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            Thread.sleep(200);
                            showcase.setTarget(new ViewTarget(R.id.vpPager, thisActivity));
                            showcase.setContentTitle("View/delete expenses of each type");
                            showcase.setContentText("After adding some expenses, you can tap each element on the chart to view them.");
                            showcase.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);
                            showcase.setButtonText("Done");
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    case 8:
                        showcase.setTarget(new ViewTarget(R.id.popupmenu, thisActivity));
                        showcase.setContentTitle("Menu");
                        showcase.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);
                        showcase.setContentText("Tap here to view this guide again, or view a list of each expense type.");
                        break;
                    default:
                        showcase.hide();
                        showcase = null;
                }
            }
        });
    }
}