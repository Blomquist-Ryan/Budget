package com.example.budget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements BudgetItemAdditionModal.BudgetAdditionDialogListener{

    private static final String PERCENTBUDGETDATA = "PercentBudgetData";
    private static final String DOLLARBUDGETDATA = "DollarBudgetData";
    private static final String TITHINGDATA = "TithingData";
    private static final String TOTALSDATA = "TotalsData";
    private ArrayList<BudgetItem> dollarBudget;
    private TextView dollarTotalAmount;
    private ArrayList<BudgetItem> percentBudget;
    private RecyclerView percentRecyclerView;
    private ArrayList<String> totals;
    private RecyclerView dollarRecyclerView;
    private BudgetRecyclerAdapter dollarAdapter;
    private TextView percentTotalAmount;
    private BudgetRecyclerAdapter percentAdapter;
    private ImageButton addDollarBudgetItem;
    private ImageButton addPercentBudgetItem;
    private ImageButton spendTithing;
    private Button addFunds;
    private EditText changeAmount;
    private TextView tithing;
    private TextView tithingFunds;
    private TextView theoryPercent;
    private TextView percentTotal;
    private BudgetItem tithes;
    private float money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load(this);
        setUI();
        setOnClicks();

    }

    private void setOnClicks()
    {
        setDollarAdapter();
        setPercentAdapter();
        setDollarAdditionOnClick();
        setPercentAdditionOnClick();
        setAddFundsOnClick();
        setAmountChanger();
        setSpendTithingOnClick();
    }

    private void setUI()
    {

        changeAmount = findViewById(R.id.amountToAdd);
        addFunds = findViewById(R.id.addFunds);
        percentRecyclerView = findViewById(R.id.percentItems);
        dollarRecyclerView = findViewById(R.id.dollarItems);
        addPercentBudgetItem = findViewById(R.id.percentItemAdd);
        addDollarBudgetItem = findViewById(R.id.dollarItemAdd);
        theoryPercent = findViewById(R.id.theoryPercentage);
        spendTithing = findViewById(R.id.spendtithing);
        tithing = findViewById(R.id.tithing);
        tithingFunds = findViewById(R.id.tithingfunds);
        tithingFunds.setText(String.valueOf(tithes.getTotal()));
        percentTotalAmount = findViewById(R.id.percenttotalamount);
        dollarTotalAmount = findViewById(R.id.dollartotalamount);
        percentTotal = findViewById(R.id.totalpercent);
//        loadTotals(this);
    }


    private void setDollarAdapter(){
        dollarAdapter = new BudgetRecyclerAdapter(R.layout.dollaritem, dollarBudget);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        dollarRecyclerView.setLayoutManager(layoutManager);

        dollarRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dollarRecyclerView.setAdapter(dollarAdapter);

        setReorder(dollarRecyclerView, true);
        setAdapterButtonClicks(dollarAdapter, true);
    }

    private void setPercentAdapter(){
        percentAdapter = new BudgetRecyclerAdapter(R.layout.percentitem, percentBudget);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        percentRecyclerView.setLayoutManager(layoutManager);

        percentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        percentRecyclerView.setAdapter(percentAdapter);

        setReorder(percentRecyclerView, false);

        setAdapterButtonClicks(percentAdapter, false);
    }

    private void setAdapterButtonClicks(BudgetRecyclerAdapter adapter, boolean isDollarItem) {
        adapter.setOnItemClickListener(new BudgetRecyclerAdapter.OnItemClickListener() {

            @Override
            public void add(int position) {
                if(isDollarItem)
                {
                    dollarBudget.get(position).Add(money);

                    dollarBudget.get(position).total = Math.round(dollarBudget.get(position).total);
                }
                else
                {
                    percentBudget.get(position).Add(money);

                    percentBudget.get(position).total = Math.round(percentBudget.get(position).total);
                }
                update(false);

            }

            @Override
            public void spend(int position) {
                if(isDollarItem)
                {
                    dollarBudget.get(position).Subtract(money);
                    dollarBudget.get(position).total = Math.round(dollarBudget.get(position).total);
                }
                else
                {
                    percentBudget.get(position).Subtract(money);
                    percentBudget.get(position).total = Math.round(percentBudget.get(position).total);
                }
                update(false);

            }

            @Override
            public void remove(int position) {
                if(isDollarItem)
                {
                    dollarBudget.remove(position);
                    update(true);
                }
                else
                {
                    percentBudget.remove(position);
                    update(false);
                }
            }

            @Override
            public void changeAmount(int position) {
                if(isDollarItem)
                {
                    dollarBudget.get(position).setAmount(money);
                    update(true);
                }
                else
                {
                    percentBudget.get(position).setAmount(money);
                    update(false);
                }
            }
        });
    }


    public void setReorder(RecyclerView view, boolean isDollarItem)
    {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                if(isDollarItem)
                {
                    Collections.swap(dollarBudget, from, to);
                }
                else
                {
                    Collections.swap(percentBudget, from, to);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                //recyclerView.getAdapter().notifyItemMoved(from, to);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(view);

    }

    public void save(Context context)
    {
        savePercentBudget(context, percentBudget);
        saveDollarBudget(context, dollarBudget);
        saveTithing(context, tithes);
//        SaveTotals(context, totals);
    }

//    private void SaveTotals(Context context, ArrayList<String> totals)
//    {
//        Gson gson = new Gson();
//        SharedPreferences preferences = context.getSharedPreferences(TOTALSDATA, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        String json = gson.toJson(totals);
//        editor.putString(TOTALSDATA, json);
//        editor.apply();
//    }

    public void savePercentBudget(Context context, ArrayList<BudgetItem> budgetData)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(PERCENTBUDGETDATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String json = gson.toJson(budgetData);
        editor.putString(PERCENTBUDGETDATA, json);
        editor.apply();
    }

    public void saveTithing(Context context,BudgetItem tithing)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(TITHINGDATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String json = gson.toJson(tithing);
        editor.putString(TITHINGDATA, json);
        editor.apply();
    }

    public void saveDollarBudget(Context context, ArrayList<BudgetItem> budgetData)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(DOLLARBUDGETDATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String json = gson.toJson(budgetData);
        editor.putString(DOLLARBUDGETDATA, json);
        editor.apply();
    }
    public void load(Context context)
    {
        loadDollarBudget(context);
        loadPercentBudget(context);
        loadTithing(context);
//        loadTotals(context);
    }

//    private void loadTotals(Context context)
//    {
//        Gson gson = new Gson();
//        SharedPreferences preferences = context.getSharedPreferences(TOTALSDATA, MODE_PRIVATE);
//        String json = preferences.getString(TOTALSDATA, null);
//        Type type = new TypeToken<String>() {}.getType();
//        totals = gson.fromJson(json, type);
//
//        if(isStringArrayNullOrEmpty(totals))
//        {
//            totals = new  ArrayList<>();
//            totals.add("");
//            totals.add("");
//            totals.add("");
//        }
//        else
//        {
//            dollarTotalAmount.setText(totals.get(0));
//            percentTotal.setText(totals.get(1));
//            percentTotalAmount.setText(totals.get(2));
//        }
//
//
//    }

    public void loadDollarBudget(Context context)//ArrayList<BudgetItem> loadDollarBudget(Context context)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(DOLLARBUDGETDATA, MODE_PRIVATE);
        String json = preferences.getString(DOLLARBUDGETDATA, null);
        Type type = new TypeToken<ArrayList<BudgetItem>>() {}.getType();
        dollarBudget = gson.fromJson(json, type);

        if(isNullOrEmpty(dollarBudget))
        {
            dollarBudget = new  ArrayList<>();
        }
    }

    public void loadTithing(Context context)//ArrayList<BudgetItem> loadPercentBudget(Context context)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(TITHINGDATA, MODE_PRIVATE);
        String json = preferences.getString(TITHINGDATA, null);
        Type type = new TypeToken<BudgetItem>() {}.getType();
        tithes = gson.fromJson(json, type);

        if(tithes == null)
        {
            tithes = new BudgetItem("Tithing", 0);
        }
    }

    public void loadPercentBudget(Context context)//ArrayList<BudgetItem> loadPercentBudget(Context context)
    {
        Gson gson = new Gson();
        SharedPreferences preferences = context.getSharedPreferences(PERCENTBUDGETDATA, MODE_PRIVATE);
        String json = preferences.getString(PERCENTBUDGETDATA, null);
        Type type = new TypeToken<ArrayList<BudgetItem>>() {}.getType();
        percentBudget = gson.fromJson(json, type);

        if(isNullOrEmpty(percentBudget))
        {
            percentBudget = new  ArrayList<>();
        }
    }

    private boolean isNullOrEmpty(ArrayList<BudgetItem> list)
    {
        if (list == null)
        {
            return true;
        }
        if(list.isEmpty())
        {
            return true;
        }
        return false;
    }
    private boolean isStringArrayNullOrEmpty(ArrayList<String> list)
    {
        if (list == null)
        {
            return true;
        }
        if(list.isEmpty())
        {
            return true;
        }
        return false;
    }
    private void setPercentAdditionOnClick()
    {
        addPercentBudgetItem.setOnClickListener(view -> openDialog(false));
    }

    private void setDollarAdditionOnClick()
    {
        addDollarBudgetItem.setOnClickListener(view -> openDialog(true));
    }

    private void setSpendTithingOnClick()
    {
        spendTithing.setOnClickListener(view -> tithe() );
    }

    private void tithe() {
        tithes.Subtract(tithes.getTotal());
        update(false);
    }

    public void setAddFundsOnClick()
    {
        addFunds.setOnClickListener(view -> AddFunds());
    }

    public void openDialog(boolean isDollarItem)
    {
        BudgetItemAdditionModal addition = new BudgetItemAdditionModal(isDollarItem);
        addition.show(getSupportFragmentManager(), "addtobudget");
    }

    public void AddFunds()
    {
        float tempMoney =  money;
        tithes.Add((tempMoney * .1f));
        tempMoney -= (tempMoney * .1f);
        for (BudgetItem item: dollarBudget)
        {
            if (tempMoney < item.getAmount())
            {
                item.Add(tempMoney);
                tempMoney -= tempMoney;
                break;
            }
            else
            {
                item.Add(item.getAmount());
                tempMoney -= item.getAmount();
            }

        }
        if (tempMoney > 0)
        {
            for (BudgetItem item: percentBudget)
            {
                float percent = item.getAmount() / 100;
//                tempMoney = Math.round(tempMoney);
                item.Add(Math.round(tempMoney*percent));
            }
        }
        update(false);
    }

    public void calculateTheoreticalPercentage()
    {
        float tempMoney = money;
        for (BudgetItem item: dollarBudget)
        {
            tempMoney -= item.getAmount();

        }
        if(tempMoney <= 0 )
        {
            theoryPercent.setText("0");
        }
        else
        {
            theoryPercent.setText(String.valueOf(tempMoney));
        }
    }



    private void setAmountChanger() {changeAmount.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s.toString()) || s.toString().equalsIgnoreCase("."))
            {
                money = 1;
            }
            else
            {
                money = Float.parseFloat(s.toString());
            }
            calculateTheoreticalPercentage();
        }
        });
    }
    public void calculateDollarTotal()
    {
        float tempTotal = 0;
        for (BudgetItem item : dollarBudget)
        {
           tempTotal += item.getTotal();
        }
        String dollarTotal = String.valueOf(tempTotal);
//        totals.set(0, dollarTotal);
        dollarTotalAmount.setText(dollarTotal);

    }
    public void calculatePercentageTotals()
    {
        float tempTotal$ = 0;
        float tempTotalPercentage = 0;

        for (BudgetItem item : percentBudget)
        {
            tempTotal$ += item.getTotal();
            tempTotalPercentage += item.getAmount();

        }
        String totalMoney = String.valueOf(Math.round(tempTotal$));
        String totalPercent =String.valueOf(tempTotalPercentage);

//        totals.set(1, totalPercent);
//        totals.set(2, totalMoney);
        percentTotalAmount.setText(totalMoney);
        percentTotal.setText(totalPercent);

    }

    public void update(boolean dollarItemsChanged) {
        if (dollarItemsChanged)
        {
            calculateTheoreticalPercentage();

        }
        calculateDollarTotal();
        calculatePercentageTotals();
        dollarAdapter.notifyDataSetChanged();
        percentAdapter.notifyDataSetChanged();
        tithingFunds.setText(String.valueOf(tithes.getTotal()));
        save(this);
    }

    @Override
    public void applyAddition(String name, float amount, boolean isDollarItem)
    {
        BudgetItem newBudgetItem = new BudgetItem(name, amount);

        if(isDollarItem)
        {
            dollarBudget.add(newBudgetItem);
            update(true);
        }
        else
        {
            percentBudget.add(newBudgetItem);
            update(false);
        }


    }
}