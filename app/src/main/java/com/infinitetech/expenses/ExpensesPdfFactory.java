package com.infinitetech.expenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.infinitetech.expenses.SqliteHandlers.ExpensesTableHandler;
import com.infinitetech.expenses.SqliteHandlers.IncomeTableHandler;
import com.infinitetech.expenses.TransactionTypes.Expense;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hossam on 2/22/2015.
 */
public class ExpensesPdfFactory extends BroadcastReceiver{

    static FileOutputStream fileOutputStream ;
    Context context;
    static File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) , "Expenses.pdf");

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

            createPdfTask createPdfTask = new createPdfTask();
            createPdfTask.execute(context);
    }


    private void createPdf(Context context) throws DocumentException {
        Document document = new Document();
        ExpensesTableHandler expensesTableHandler = new ExpensesTableHandler(context);
        IncomeTableHandler incomeTableHandler = new IncomeTableHandler(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("ExpensesApp" , Context.MODE_PRIVATE);
        PdfWriter.getInstance(document, fileOutputStream);
        document.open();
        Paragraph preface = new Paragraph("Expenses of:  " + getDate());
        preface.setAlignment(Element.ALIGN_CENTER);
        preface.setSpacingAfter(10);
        document.add(preface);
        Paragraph expensesOfTheDay = new Paragraph("Expenses of the day : " + expensesTableHandler.getSpendingOfTheDay()+ " L.E");
        expensesOfTheDay.setAlignment(Element.ALIGN_CENTER);
        expensesOfTheDay.setSpacingAfter(10);
        Paragraph remainingMoney = new Paragraph("Remaining Money : " +  sharedPreferences.getString("remaining" , "0.0"));
        remainingMoney.setAlignment(Element.ALIGN_CENTER);
        remainingMoney.setSpacingAfter(20);
        Paragraph totalExpensesOfMonth = new Paragraph("Expenses of the month : "  + expensesTableHandler.getMonthSpending() + " L.E");
        totalExpensesOfMonth.setAlignment(Element.ALIGN_RIGHT);
        Paragraph totalIncomeMonth = new Paragraph("Total Income this month : " + incomeTableHandler.getTotalIncomeInWeek()+ " L.E");
        totalIncomeMonth.setAlignment(Element.ALIGN_RIGHT);
        Paragraph expenseOfTheWeek = new Paragraph("Total Expenses Of the week : " + expensesTableHandler.getWeekSpending()+ " L.E");
        expenseOfTheWeek.setAlignment(Element.ALIGN_LEFT);
        Paragraph totalIncomeOfWeek = new Paragraph("Total Income this week : " + incomeTableHandler.getTotalIncomeInWeek()+ " L.E");
        totalIncomeOfWeek.setAlignment(Element.ALIGN_LEFT);
        totalIncomeOfWeek.setSpacingAfter(30);
        document.add(expensesOfTheDay);
        document.add(remainingMoney);
        document.add(totalExpensesOfMonth);
        document.add(totalIncomeMonth);
        document.add(expenseOfTheWeek);
        document.add(totalIncomeOfWeek);
        document.add(insertMainRow());
        for (Expense expense : expensesTableHandler.getExpensesOfTheDay()) {
            document.add(insertRow(expense));
        }
        document.close();
    }

    private static PdfPTable insertRow (Expense expense) {
        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Phrase(expense.getName())));
        table.addCell(new PdfPCell(new Phrase(expense.getAmount() + "")));
        table.addCell(new PdfPCell(new Phrase(expense.getCategory().name())));
        table.addCell(new PdfPCell(new Phrase(expense.getFormattedDate())));
        return table ;
    }

    private static PdfPTable insertMainRow(){
        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Phrase("Name")));
        table.addCell(new PdfPCell(new Phrase("Price")));
        table.addCell(new PdfPCell(new Phrase("Category")));
        table.addCell(new PdfPCell(new Phrase("Date")));
        table.setSpacingAfter(15);
        return table ;
    }



    private static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd\\MM\\yyyy");
        Date date1 = new Date();
        return simpleDateFormat.format(date1);
    }

    private class createPdfTask extends AsyncTask<Context ,Void ,Void>{

        @Override
        protected Void doInBackground(Context... params) {
            try {
                createPdf(context);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            callSuperToastNormal("File created");
        }

    }

    private void callSuperToastNormal(String Text) {
        SuperToast.create(context, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.GREEN, SuperToast.Animations.SCALE)).show();
    }

    public static File getFile() {
        return file;
    }
}
