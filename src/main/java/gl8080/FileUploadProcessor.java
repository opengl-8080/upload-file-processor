package gl8080;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class FileUploadProcessor {

    private InputStream in;
    private FileLineProcessor lineProcessor;
    private FileDecoder decoder;
    
    public FileUploadProcessor(InputStream in, FileDecoder decoder, FileLineProcessor lineProcessor) {
        this.in = in;
        this.lineProcessor = lineProcessor;
        this.decoder = decoder;
    }

    public void execute() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(this.in, Charset.forName("Shift_JIS")));
            
            String line;
            
            while ((line = br.readLine()) != null) {
                FileLine fileLine = this.decoder.decode(line);
                this.lineProcessor.process(fileLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
