package com.se2.hanuairline.service;


public class ConvertorService {
//	public static void htmlToPdf(String fileName) {
//		// Create HTML load options
//		HtmlLoadOptions htmloptions = new HtmlLoadOptions();
//		// Load HTML file
//		Document doc = new Document("HTML-Document.html", htmloptions); 
//		// Convert HTML file to PDF
//		doc.save("HTML-to-PDF.pdf");
//	}
//	public static void main(String [] args) {
//	 htmlToPdf("/hanuairline/src/main/resources/templates/email-ticket.html");
//	}
////   public static void main(String[] args) {
////     try {
////      // Source HTML file
////      String inputHTML = "/hanuairline/src/main/resources/templates/email-ticket.html";
////      // Generated PDF file name
////      String outputPdf = "/hanuairline/src/main/resources/pdfFiles/email-ticket.pdf";
////      htmlToPdf(inputHTML, outputPdf);	      
////    } catch (IOException e) {
////      // TODO Auto-generated catch block
////      e.printStackTrace();
////    }
////  }
//	
//  private static Document html5ParseDocument(String inputHTML) throws IOException{
//    org.jsoup.nodes.Document doc;
//    System.out.println("parsing ...");
//    doc = Jsoup.parse(new File(inputHTML), "UTF-8");
//    System.out.println("parsing done ..." + doc);
//    return new W3CDom().fromJsoup(doc);
//  }
//	
//  private static void htmlToPdf(String inputHTML, String outputPdf) throws IOException {
//    Document doc = html5ParseDocument(inputHTML);
//    String baseUri = FileSystems.getDefault()
//              .getPath("F:/", "knpcode/Java/", "Java Programs/PDF using Java/PDFBox/")
//              .toUri()
//              .toString();
//    OutputStream os = new FileOutputStream(outputPdf);
//    PdfRendererBuilder builder = new PdfRendererBuilder();
//    builder.withUri(outputPdf);
//    builder.toStream(os);
//    // using absolute path here
//    builder.useFont(new File("F:\\knpcode\\Java\\Java Programs\\PDF using Java\\PDFBox\\Gabriola.ttf"), 
//    "Gabriola");
//    builder.withW3cDocument(doc, baseUri);
//    //builder.useUriResolver(new MyResolver());
//    builder.run();
//    System.out.println("PDF generation completed");
//    os.close();
//  }
}