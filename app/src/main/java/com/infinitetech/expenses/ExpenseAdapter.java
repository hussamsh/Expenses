package com.infinitetech.expenses;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;

import java.util.ArrayList;

/**
 * Created by Hossam on 3/12/2015.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private ArrayList<Expense> expenses ;
    private final Context context;

    public ExpenseAdapter(ArrayList<Expense> expenses , Context context) {
        this.expenses = expenses;
        this.context = context ;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_expense_card , parent ,false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.nameEditText.setText("      " + expense.getName());
        holder.nameEditText.setTextColor(Color.WHITE);
        holder.nameEditText.setEnabled(false);
        holder.priceEditText.setText("      " + expense.getPrice());
        holder.priceEditText.setTextColor(Color.WHITE);
        holder.priceEditText.setEnabled(false);

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(500);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(500);

        final View.OnClickListener save = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.editButton.startAnimation(in);
                holder.editButton.setText("Save");
                holder.editButton.setOnClickListener(edit);
            }
        };

        final View.OnClickListener edit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.editButton.startAnimation(out);
                holder.editButton.setText("Save");
                holder.editButton.setOnClickListener(save);
            }
        };
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        protected EditText nameEditText ;
        protected EditText priceEditText ;
        protected Button editButton ;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            nameEditText = (EditText) itemView.findViewById(R.id.name_edit);
            priceEditText = (EditText) itemView.findViewById(R.id.price_edit);
            editButton = (Button) itemView.findViewById(R.id.save_edit);
        }
    }

    private void callSuperToastNormal(String Text) {
        SuperToast.create(context, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

}
