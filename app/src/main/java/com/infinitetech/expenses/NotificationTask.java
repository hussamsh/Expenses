package com.infinitetech.expenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Hossam on 2/22/2015.
 */
public class NotificationTask extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        createPdfTask createPdfTask = new createPdfTask();
        createPdfTask.execute(context);
    }

    private void createPdf(Context context) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document , MainActivity.fileOutputStream);
        document.open();
        Paragraph preface = new Paragraph("Expenses");
        preface.setAlignment(Element.ALIGN_CENTER);
        preface.setSpacingAfter(30);
        document.add(preface);
        document.add(insertMainRow());
        ExpensesTableHandler handler = new ExpensesTableHandler(context);
        Cursor c = handler.getExpensesOfTheDay();
        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_NAME));
            double price = c.getDouble(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_COST));
            String category = c.getString(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_CATEGORY));
            String date = c.getString(c.getColumnIndex(ExpensesTableHandler.EXPENSES_COLUMN_DATE));
            document.add(insertRow(name, price, category, date));
        }
        document.close();
    }

    private static PdfPTable insertRow (String name , double price , String category , String date) {
        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Phrase(name)));
        table.addCell(new PdfPCell(new Phrase(price+"")));
        table.addCell(new PdfPCell(new Phrase(category)));
        table.addCell(new PdfPCell(new Phrase(date)));
        return table ;
    }

    private static PdfPTable insertMainRow(){
        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Phrase("Name")));
        table.addCell(new PdfPCell(new Phrase("Cost")));
        table.addCell(new PdfPCell(new Phrase("Category")));
        table.addCell(new PdfPCell(new Phrase("Date")));
        table.setSpacingAfter(15);
        return table ;
    }



    private class createPdfTask extends AsyncTask<Context ,Void ,Void>{

        Context context ;

        @Override
        protected Void doInBackground(Context... params) {
            context = params[0];
            try {
                createPdf(context);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            return null ;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            callSuperToastAlert("Pdf created" , context);
        }
    }

    private void callSuperToastAlert(String Text , Context context) {
        SuperToast.create(context, Text, SuperToast.Duration.VERY_SHORT, Style.getStyle(Style.RED, SuperToast.Animations.FLYIN)).show();
    }

}
