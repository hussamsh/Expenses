package com.infinitetech.expenses;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;
import com.infinitetech.expenses.TransactionTypes.Expense;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card, parent ,false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ExpenseViewHolder holder, int position) {
        final Expense expense = expenses.get(position);
        holder.nameEditText.setText(expense.getName());
        holder.nameEditText.setTextColor(Color.WHITE);
        holder.priceEditText.setText("" + expense.getAmount());
        holder.priceEditText.setTextColor(Color.WHITE);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator animator = ValueAnimator.ofInt(1 , 100);
                animator.setDuration(1000);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        holder.editButton.setProgress(value);
                    }
                });
                ExpensesTableHandler handler = new ExpensesTableHandler(context);
                handler.editExpense(holder.nameEditText.getText().toString() , Double.parseDouble(holder.priceEditText.getText().toString()) ,holder.categoryButton.getText().toString() , expense.getDate());
                animator.start();

            }
        });

        holder.categoryButton.setText(expense.getCategory().name());
        holder.categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDroppyFromXml(holder.categoryButton);
    }

    private void initDroppyFromXml(final Button button){
        final String category ;
        DroppyMenuPopup.Builder builder = new DroppyMenuPopup.Builder(context, button);
        DroppyMenuPopup menu = builder.fromMenu(R.menu.category_menu)
                .triggerOnAnchorClick(false)
                .setOnClick(new DroppyClickCallbackInterface() {
                    @Override
                    public void call(View v, int id) {
                        switch (id){
                            case R.id.food_itemMenu:
                                 button.setText("Food");
                                break;
                            case R.id.household_itemMenu:
                                 button.setText("Household");
                                break;
                            case R.id.personal_itemMenu:
                                button.setText("Personal");
                                break;
                        }
                    }
                })
                .build();
        menu.show();
    }
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        protected EditText nameEditText ;
        protected EditText priceEditText ;
        protected CircularProgressButton editButton ;
        protected Button categoryButton ;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            nameEditText = (EditText) itemView.findViewById(R.id.name_edit);
            priceEditText = (EditText) itemView.findViewById(R.id.price_edit);
            editButton = (CircularProgressButton) itemView.findViewById(R.id.save_edit);
            categoryButton = (Button) itemView.findViewById(R.id.category_btn);
        }
    }

    private void callSuperToastNormal(String Text) {
        SuperToast.create(context, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.BLUE, SuperToast.Animations.FLYIN)).show();
    }

}
