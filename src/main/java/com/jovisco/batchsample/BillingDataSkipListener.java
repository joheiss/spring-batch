package com.jovisco.batchsample;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.lang.NonNull;

public class BillingDataSkipListener implements SkipListener<BillingData, BillingData> {

    Path skippedItemsFile;

    public BillingDataSkipListener(String skippedItemsFile) {
        this.skippedItemsFile = Paths.get(skippedItemsFile);
    }

    @Override
    public void onSkipInRead(@NonNull Throwable t) {
        if (t instanceof FlatFileParseException exception) {
            var rawLine = exception.getInput();
            var lineNumber = exception.getLineNumber();
            var skippedLine = lineNumber + "|" + rawLine + System.lineSeparator();

            try {
                Files.writeString(skippedItemsFile, skippedLine, StandardOpenOption.APPEND, StandardOpenOption.CREATE);

            } catch (IOException e) {
                throw new RuntimeException("Unable to write skipped item: " + skippedLine);
            }
        }
    }

    
    
}
