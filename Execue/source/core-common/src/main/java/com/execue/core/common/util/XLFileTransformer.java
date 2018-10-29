/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.execue.core.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.execue.core.common.type.PublishedFileType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;

public class XLFileTransformer implements HSSFListener {

   private FormatTrackingHSSFListener    formatListener;
   private boolean                       outputFormulaValues = true;
   private SheetRecordCollectingListener workbookBuildingListener;
   private POIFSFileSystem               poiFSFileSystem;
   private PrintStream                   printStream;
   private ArrayList                     boundSheetRecords   = new ArrayList();
   private HSSFWorkbook                  stubWorkbook;
   private int                           sheetIndex          = -1;
   private BoundSheetRecord[]            orderedBSRs;
   private int                           nextRow;
   private int                           nextColumn;
   private boolean                       outputNextStringRecord;
   private SSTRecord                     sstRecord;

   private int                           lastRowNumber;
   private int                           lastColumnNumber;
   private int                           minColumns;
   private static String                 FORWARD_SLASH       = "/";
   private static String                 PERIOD              = ".";

   public String convertXLFileToCSV (String xlFileName, Long applicationId, String csvFileStoragePath)
            throws ExeCueException {
      String xlName = xlFileName.substring(xlFileName.lastIndexOf(FORWARD_SLASH) + 1, xlFileName.indexOf(PERIOD));
      String csvFileName = csvFileStoragePath + FORWARD_SLASH + xlName + PERIOD
               + PublishedFileType.CSV.getValue().toLowerCase();

      try {
         poiFSFileSystem = new POIFSFileSystem(new FileInputStream(xlFileName));
         printStream = new PrintStream(new File(csvFileName));
         MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
         formatListener = new FormatTrackingHSSFListener(listener);

         HSSFEventFactory factory = new HSSFEventFactory();
         HSSFRequest request = new HSSFRequest();

         if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
         } else {
            workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
         }

         factory.processWorkbookEvents(request, poiFSFileSystem);
      } catch (FileNotFoundException fileNotFoundException) {
         fileNotFoundException.printStackTrace();
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, fileNotFoundException);
      } catch (IOException ioException) {
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, ioException);
      }

      return csvFileName;
   }

   public void processRecord (Record record) {
      int thisRow = -1;
      int thisColumn = -1;
      String thisStr = null;
      switch (record.getSid()) {
         case BoundSheetRecord.sid:
            boundSheetRecords.add(record);
            break;
         case BOFRecord.sid:
            BOFRecord br = (BOFRecord) record;
            if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
               // Create sub workbook if required
               if (workbookBuildingListener != null && stubWorkbook == null) {
                  stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
               }
               sheetIndex++;
               if (orderedBSRs == null) {
                  orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
               }
            }
            break;

         case SSTRecord.sid:
            sstRecord = (SSTRecord) record;
            break;

         case BlankRecord.sid:
            BlankRecord brec = (BlankRecord) record;
            thisRow = brec.getRow();
            thisColumn = brec.getColumn();
            thisStr = "";
            break;
         case BoolErrRecord.sid:
            BoolErrRecord berec = (BoolErrRecord) record;

            thisRow = berec.getRow();
            thisColumn = berec.getColumn();
            thisStr = "";
            break;

         case FormulaRecord.sid:
            FormulaRecord frec = (FormulaRecord) record;

            thisRow = frec.getRow();
            thisColumn = frec.getColumn();

            if (outputFormulaValues) {
               if (Double.isNaN(frec.getValue())) {
                  // Formula result is a string
                  // This is stored in the next record
                  outputNextStringRecord = true;
                  nextRow = frec.getRow();
                  nextColumn = frec.getColumn();
               } else {
                  thisStr = formatListener.formatNumberDateCell(frec);
               }
            } else {
               thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
            }
            break;
         case StringRecord.sid:
            if (outputNextStringRecord) {
               // String for formula
               StringRecord srec = (StringRecord) record;
               thisStr = srec.getString();
               thisRow = nextRow;
               thisColumn = nextColumn;
               outputNextStringRecord = false;
            }
            break;

         case LabelRecord.sid:
            LabelRecord lrec = (LabelRecord) record;

            thisRow = lrec.getRow();
            thisColumn = lrec.getColumn();
            thisStr = '"' + lrec.getValue() + '"';
            break;
         case LabelSSTRecord.sid:
            LabelSSTRecord lsrec = (LabelSSTRecord) record;

            thisRow = lsrec.getRow();
            thisColumn = lsrec.getColumn();
            if (sstRecord == null) {
               thisStr = '"' + "(No SST Record, can't identify string)" + '"';
            } else {
               thisStr = '"' + sstRecord.getString(lsrec.getSSTIndex()).toString() + '"';
            }
            break;
         case NoteRecord.sid:
            NoteRecord nrec = (NoteRecord) record;

            thisRow = nrec.getRow();
            thisColumn = nrec.getColumn();
            // TODO: Find object to match nrec.getShapeId()
            thisStr = '"' + "(TODO)" + '"';
            break;
         case NumberRecord.sid:
            NumberRecord numrec = (NumberRecord) record;

            thisRow = numrec.getRow();
            thisColumn = numrec.getColumn();

            // Format
            thisStr = formatListener.formatNumberDateCell(numrec);
            break;
         case RKRecord.sid:
            RKRecord rkrec = (RKRecord) record;

            thisRow = rkrec.getRow();
            thisColumn = rkrec.getColumn();
            thisStr = '"' + "(TODO)" + '"';
            break;
         default:
            break;
      }

      // Handle new row
      if (thisRow != -1 && thisRow != lastRowNumber) {
         lastColumnNumber = -1;
      }

      // Handle missing column
      if (record instanceof MissingCellDummyRecord) {
         MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
         thisRow = mc.getRow();
         thisColumn = mc.getColumn();
         thisStr = "";
      }

      // If we got something to print out, do so
      if (thisStr != null) {
         if (thisColumn > 0) {
            printStream.print(',');
         }
         printStream.print(thisStr);
      }

      // Update column and row count
      if (thisRow > -1)
         lastRowNumber = thisRow;
      if (thisColumn > -1)
         lastColumnNumber = thisColumn;

      // Handle end of row
      if (record instanceof LastCellOfRowDummyRecord) {
         // Print out any missing commas if needed
         if (minColumns > 0) {
            // Columns are 0 based
            if (lastColumnNumber == -1) {
               lastColumnNumber = 0;
            }
            for (int i = lastColumnNumber; i < (minColumns); i++) {
               printStream.print(',');
            }
         }

         // We're onto a new row
         lastColumnNumber = -1;

         // End the row
         printStream.println();
      }
   }

   public String getFormattedFileNameForStorage (String originalFileName, Long applicationId) {
      String destinationFilename = ExecueCoreUtil.getFormattedSystemCurrentMillis() + "_"
               + RandomStringUtils.randomAlphabetic(3) + "_" + applicationId + "_" + originalFileName;
      return destinationFilename.toLowerCase();
   }

}
