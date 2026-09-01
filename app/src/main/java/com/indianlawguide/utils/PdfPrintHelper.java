package com.indianlawguide.utils;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.indianlawguide.database.entities.LawEntity;

public class PdfPrintHelper {

    public static void printLawToPdf(Context context, LawEntity law) {
        if (law == null || context == null) return;

        WebView webView = new WebView(context);
        String htmlContent = buildHtmlDocument(law);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
                if (printManager != null) {
                    String jobName = "IndianLawGuide_" + law.getTitle().replaceAll("[^a-zA-Z0-9]", "_");
                    PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);
                    PrintAttributes attributes = new PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 300, 300))
                        .setMinMargins(PrintAttributes.Margins.NARROW)
                        .build();
                    printManager.print(jobName, printAdapter, attributes);
                }
            }
        });

        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null);
    }

    private static String buildHtmlDocument(LawEntity law) {
        return "<!DOCTYPE html>" +
            "<html><head><meta charset='utf-8'>" +
            "<style>" +
            "body { font-family: 'Helvetica Neue', Arial, sans-serif; padding: 24px; color: #111; line-height: 1.6; }" +
            ".header { border-bottom: 2px solid #0B3C5D; padding-bottom: 12px; margin-bottom: 16px; }" +
            ".app-title { font-size: 13px; color: #0B3C5D; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; }" +
            "h1 { font-size: 22px; color: #0B3C5D; margin: 8px 0 4px 0; }" +
            ".badge { display: inline-block; padding: 4px 10px; background: #D2E4FF; color: #001D36; border-radius: 6px; font-size: 12px; font-weight: bold; margin-bottom: 12px; }" +
            ".section-box { background: #F8F9FC; border-left: 4px solid #0B3C5D; padding: 12px 16px; margin: 14px 0; border-radius: 0 8px 8px 0; }" +
            ".section-title { font-size: 14px; font-weight: bold; color: #0B3C5D; margin-bottom: 6px; }" +
            ".punishment { background: #FFEBEE; border-left: 4px solid #C62828; color: #B71C1C; padding: 10px 14px; margin: 12px 0; border-radius: 0 8px 8px 0; font-weight: bold; }" +
            ".dos { background: #E8F5E9; border-left: 4px solid #2E7D32; padding: 10px 14px; margin: 10px 0; border-radius: 0 8px 8px 0; }" +
            ".donts { background: #FFEBEE; border-left: 4px solid #C62828; padding: 10px 14px; margin: 10px 0; border-radius: 0 8px 8px 0; }" +
            ".disclaimer { font-size: 10px; color: #777; margin-top: 30px; border-top: 1px solid #ccc; padding-top: 8px; font-style: italic; }" +
            "</style></head><body>" +
            "<div class='header'>" +
            "<div class='app-title'>Indian Law Pocket Guide • Offline Legal Citizen Handbook</div>" +
            "<h1>" + law.getTitle() + "</h1>" +
            "<div class='badge'>" + law.getCategory() + "</div>" +
            "</div>" +
            "<div class='section-box'><div class='section-title'>Overview & Summary</div>" + law.getSummary() + "</div>" +
            "<div class='section-box'><div class='section-title'>Detailed Legal Rights</div>" + law.getDescription() + "</div>" +
            "<div class='section-box'><div class='section-title'>Key Protections</div>" + law.getRights() + "</div>" +
            "<div class='dos'><div class='section-title' style='color:#1B5E20;'>What You Should Do (Do's)</div>" + law.getDos() + "</div>" +
            "<div class='donts'><div class='section-title' style='color:#B71C1C;'>What You Must Avoid (Don'ts)</div>" + law.getDonts() + "</div>" +
            "<div class='section-box'><div class='section-title'>Statutory Law & Section</div>" + law.getLawName() + " — " + law.getSection() + "</div>" +
            "<div class='punishment'>Penalty / Consequence: " + law.getPunishment() + "</div>" +
            "<div class='section-box'><div class='section-title'>Emergency Helpline</div>" + law.getHelpline() + "</div>" +
            "<div class='disclaimer'>Disclaimer: This document is generated for informational and educational awareness only. It does NOT constitute legal advice. Please consult a qualified advocate for official legal counsel.</div>" +
            "</body></html>";
    }
}
