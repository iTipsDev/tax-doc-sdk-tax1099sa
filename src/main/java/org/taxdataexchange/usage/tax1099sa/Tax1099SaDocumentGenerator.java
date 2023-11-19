package org.taxdataexchange.usage.tax1099sa;

import org.taxdataexchange.core.utils.*;
import org.taxdataexchange.docs.tax1099sa.models.*;
import org.taxdataexchange.docs.tax1099sa.utils.*;

public class Tax1099SaDocumentGenerator {

    public static TaxDataList sampleData( ) {

        // -----------------------------------------------------------------
        // Sample data
        // -----------------------------------------------------------------
        TaxData taxData = SampleData.sampleTaxData( "Tax1099Sa" );
        TaxDataList taxDataList = new TaxDataList( );
        taxDataList.addFormsItem( taxData );

        return taxDataList;

    }

    public static byte[] sampleQrAsPng( ) {

        // -----------------------------------------------------------------
        // Generate png
        // -----------------------------------------------------------------

        TaxDataList taxDataList = sampleData( );

        Tax1099SaPdfBuilder pdfBuilder = new Tax1099SaPdfBuilder( );
        byte[] bytes = pdfBuilder.buildQr( taxDataList );

        String filePath = "samples/Tax1099Sa.sample.png";
        FileUtils.bytesToFile( bytes, filePath );
        System.out.println( filePath );

        return bytes;

    }


    public static void samplePdf( ) {

        // -----------------------------------------------------------------
        // Generate PDF
        // -----------------------------------------------------------------

        TaxDataList taxDataList = sampleData( );

        String watermarkText = "Sample"; // Empty string for no watermark

        Tax1099SaPdfBuilder pdfBuilder = new Tax1099SaPdfBuilder( );
        byte[] pdfBytes = pdfBuilder.build( taxDataList, watermarkText );

        String filePath = "samples/Tax1099Sa.sample.pdf";
        FileUtils.bytesToFile( pdfBytes, filePath );
        System.out.println( filePath );

    }

    public static void addTrailerPage() {

        String filePath = "samples/Tax1099Sa.original.pdf";
        byte[] pdfBytes = FileUtils.readByteArray( filePath );

        // QR code including frame. See Tax1099Sa.sample.png
        byte[] pngBytes = sampleQrAsPng( );

        // Place on existing pdf
        byte[] combinedBytes = TrailerPageAdder.insertPngIntoPdf( pngBytes, pdfBytes );
        String filePath2 = "samples/Tax1099Sa.trailer.pdf";
        FileUtils.bytesToFile( combinedBytes, filePath2 );

    }

    public static void addCoverPage() {

        String filePath = "samples/Tax1099Sa.original.pdf";
        byte[] pdfBytes = FileUtils.readByteArray( filePath );

        // QR code including frame. See Tax1099Sa.sample.png
        byte[] pngBytes = sampleQrAsPng( );

        // Place on existing pdf
        byte[] combinedBytes = CoverPageAdder.insertPngIntoPdf( pngBytes, pdfBytes );
        String filePath2 = "samples/Tax1099Sa.cover.pdf";
        FileUtils.bytesToFile( combinedBytes, filePath2 );

    }

    public static void insertQrCode() {

        // Existing document
        String filePath = "samples/Tax1099Sa.original.pdf";
        byte[] pdfBytes = FileUtils.readByteArray( filePath );

        // QR code including frame. See Tax1099Sa.sample.png
        byte[] pngBytes = sampleQrAsPng( );

        // Place on existing pdf
        int pageIndex = 0; // First page is zero
        float y = 72f; // From bottom. Pixels. 72 per inch.
        byte[] combinedBytes = PngInserter.insertPngIntoPdfAt(
            pngBytes,
            pdfBytes,
            pageIndex,
            y
        );
        String filePath2 = "samples/Tax1099Sa.inserted.pdf";
        FileUtils.bytesToFile( combinedBytes, filePath2 );

    }

    public static void main(String[] args) {

        System.out.println( "Tax1099SaDocumentGenerator Begin" );

        // Create a new PDF
        samplePdf( );

        // Insert the QR code on your existing PDF at a specified location
        insertQrCode( );

        // Add cover page with QR code
        addCoverPage( );

        // Add trailer page with QR code
        addTrailerPage( );

        System.out.println( "Tax1099SaDocumentGenerator Done" );

    }

}
