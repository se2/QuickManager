package model;

import java.util.ArrayList;
import org.joda.time.LocalDate;

/**
 *
 * @author SVC
 */
public class Report {

    private ArrayList<String> paidStudentsInfo;
    private ArrayList<String> unPaidStudentsInfo;
    private ArrayList<Long> paidAmount;
    private ArrayList<Long> unPaidAmount;
    private ArrayList<Invoice> paidInvoice;
    private ArrayList<Invoice> unPaidInvoice;
    private LocalDate startDate;
    private LocalDate endDate;

    public Report(ArrayList<String> paidStudentsInfo, ArrayList<String> unPaidStudentsInfo,
            ArrayList<Long> paidAmount, ArrayList<Long> unPaidAmount, ArrayList<Invoice> paidInvoice,
            ArrayList<Invoice> unPaidInvoice, LocalDate startDate, LocalDate endDate) {
        this.paidStudentsInfo = paidStudentsInfo;
        this.unPaidStudentsInfo = unPaidStudentsInfo;
        this.paidAmount = paidAmount;
        this.unPaidAmount = unPaidAmount;
        this.paidInvoice = paidInvoice;
        this.unPaidInvoice = unPaidInvoice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ArrayList<String> getPaidStudentsInfo() {
        return paidStudentsInfo;
    }

    public void setPaidStudentsInfo(ArrayList<String> paidStudentsInfo) {
        this.paidStudentsInfo = paidStudentsInfo;
    }

    public ArrayList<String> getUnPaidStudentsInfo() {
        return unPaidStudentsInfo;
    }

    public void setUnPaidStudentsInfo(ArrayList<String> unPaidStudentsInfo) {
        this.unPaidStudentsInfo = unPaidStudentsInfo;
    }

    public ArrayList<Long> getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(ArrayList<Long> paidAmount) {
        this.paidAmount = paidAmount;
    }

    public ArrayList<String> getPaidAmountString() {
        ArrayList<String> amount = new ArrayList<>(paidAmount.size());

        for (int i = 0; i < paidAmount.size(); i++) {
            String money = formatCurrency(paidAmount.get(i) + "");
            amount.add(money);
        }
        return amount;
    }

    private String formatCurrency(String money) {
        String temp = "";

        int count = 0;
        for (int i = money.length() - 1; i >= 0; i--, count++) {
            if (count % 3 == 0) {
                if (temp.equals("")) {
                    temp = money.charAt(i) + "";
                } else {
                    temp = money.charAt(i) + "," + temp;
                }
            } else {
                temp = money.charAt(i) + temp;
            }
        }
        return "VND " + temp;
    }

    public ArrayList<String> getUnPaidAmountString() {
        ArrayList<String> amount = new ArrayList<>(unPaidAmount.size());

        for (int i = 0; i < unPaidAmount.size(); i++) {
            String money = formatCurrency(unPaidAmount.get(i) + "");
            amount.add(money);
        }
        return amount;
    }

    public ArrayList<Long> getUnPaidAmount() {
        return unPaidAmount;
    }

    public void setUnPaidAmount(ArrayList<Long> unPaidAmount) {
        this.unPaidAmount = unPaidAmount;
    }

    public ArrayList<Invoice> getPaidInvoice() {
        return paidInvoice;
    }

    public void setPaidInvoice(ArrayList<Invoice> paidInvoice) {
        this.paidInvoice = paidInvoice;
    }

    public ArrayList<Invoice> getUnPaidInvoice() {
        return unPaidInvoice;
    }

    public void setUnPaidInvoice(ArrayList<Invoice> unPaidInvoice) {
        this.unPaidInvoice = unPaidInvoice;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTotalPaidAmountString() {
        long total = 0;
        for (int i = 0; i < paidAmount.size(); i++) {
            total += paidAmount.get(i);
        }
        return formatCurrency(total + "");
    }

    public String getTotalUnpaidAmountString() {
        long total = 0;
        for (int i = 0; i < unPaidAmount.size(); i++) {
            total += unPaidAmount.get(i);
        }
        return formatCurrency(total + "");
    }
}
