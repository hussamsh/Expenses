package com.infinitetech.expenses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hossam on 2/22/2015.
 */
public class ExpensesPdfFactory extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
            createPdfTask createPdfTask = new createPdfTask();
            createPdfTask.execute(context);
    }


    private void createPdf(Context context) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document , MainActivity.getFileOutputStream());
        document.open();
        Paragraph preface = new Paragraph("Expenses of:  " + getDate());
        preface.setAlignment(Element.ALIGN_CENTER);
        preface.setSpacingAfter(30);
        document.add(preface);
        document.add(insertMainRow());
        ExpensesTableHandler handler = new ExpensesTableHandler(context);
        for (Expense expense : handler.getExpensesOfTheDay()) {
            document.add(insertRow(expense));
        }
        document.close();
    }

    private static PdfPTable insertRow (Expense expense) {
        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Phrase(expense.getName())));
        table.addCell(new PdfPCell(new Phrase(expense.getPrice() + "")));
        table.addCell(new PdfPCell(new Phrase(expense.getCategory())));
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
            callSuperToastNormal("File created just Hit send", context);
        }

    }

    private void callSuperToastNormal(String Text, Context context) {
        SuperToast.create(context, Text, SuperToast.Duration.SHORT, Style.getStyle(Style.GREEN, SuperToast.Animations.SCALE)).show();
    }

}
