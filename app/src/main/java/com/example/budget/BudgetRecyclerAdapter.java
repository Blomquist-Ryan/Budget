package com.example.budget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BudgetRecyclerAdapter extends RecyclerView.Adapter<BudgetRecyclerAdapter.MyViewHolder>
{
    int resource;

        public interface OnItemClickListener{
            void add(int position);
            void spend(int position);
            void remove(int position);
            void changeAmount(int position);
        }
        public void setOnItemClickListener(BudgetRecyclerAdapter.OnItemClickListener clickListener)
        {
            this.clickListener = clickListener;
        }

        private ArrayList<BudgetItem> budgetItems;

        private BudgetRecyclerAdapter.OnItemClickListener clickListener;

        public BudgetRecyclerAdapter(int view, ArrayList<BudgetItem> budgetItems) {
            this.budgetItems = budgetItems;
            resource = view;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            private TextView total;
            private TextView name;
            private ImageButton increaseButton;
            private ImageButton spendButton;
            private Button amount;
            private ImageButton removeButton;



            public MyViewHolder(final View view, BudgetRecyclerAdapter.OnItemClickListener listener) {
                super(view);
                total = view.findViewById(R.id.subtotal);
                amount = view.findViewById(R.id.amount);
                name = view.findViewById(R.id.title);
                increaseButton = view.findViewById(R.id.add);
                spendButton = view.findViewById(R.id.spend);
                removeButton = view.findViewById(R.id.delete);
                increaseButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        if (listener != null)
                        {
                            int position = getAdapterPosition();
                            if( position != RecyclerView.NO_POSITION)
                            {
                                listener.add(position);
                            }
                        }
                    }
                });

                spendButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        if (listener != null)
                        {
                            int position = getAdapterPosition();
                            if( position != RecyclerView.NO_POSITION)
                            {
                                listener.spend(position);
                            }
                        }
                    }
                });

                removeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        if (listener != null)
                        {
                            int position = getAdapterPosition();
                            if( position != RecyclerView.NO_POSITION)
                            {
                                listener.remove(position);
                            }
                        }
                    }
                });

                amount.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {

                        if (listener != null)
                        {
                            int position = getAdapterPosition();
                            if( position != RecyclerView.NO_POSITION)
                            {
                                listener.changeAmount(position);
                            }
                        }
                    }
                });
            }
        }


        @NonNull
        @Override
        public BudgetRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View ingredientView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            return new BudgetRecyclerAdapter.MyViewHolder(ingredientView, clickListener);
        }

        @Override
        public int getItemCount()
        {
            return budgetItems.size();
        }

        @Override
        public void onBindViewHolder(@NonNull BudgetRecyclerAdapter.MyViewHolder holder, int position)
        {
//            DecimalFormat df = new DecimalFormat("0.0");
//            df.setRoundingMode(RoundingMode.DOWN);
            String name = budgetItems.get(position).getName();
            float amount = budgetItems.get(position).getAmount();
            BudgetItem total = budgetItems.get(position);

            holder.name.setText(name);
            holder.amount.setText(String.valueOf(amount));
            holder.total.setText(total.toString());
        }
    }
