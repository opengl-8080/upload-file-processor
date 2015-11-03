package gl8080;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileUploadProcessorTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    private File csv;
    private File fixed;
    
    @Before
    public void setup() throws Exception{
        csv = new File(tmp.getRoot(), "test.csv");
        Files.write(csv.toPath(), Arrays.asList("hoge,fuga1,piyo,43", "aaa,fuga2,bbb,59"), Charset.forName("Shift_JIS"), StandardOpenOption.CREATE);
        
        fixed = new File(tmp.getRoot(), "fixed.txt");
        Files.write(fixed.toPath(), Arrays.asList("hogefugapiyo", "aaaabbbbcccc"), Charset.forName("Shift_JIS"), StandardOpenOption.CREATE);
    }
    
    @Test
    public void 固定長ファイルも解析できること() throws Exception {
        // setup
        FieldDefinition hogeDef = FieldDefinition.newDefinition()
                                                 .name("HOGE")
                                                 .position(1, 4)
                                                 .build();

        FieldDefinition fugaDef = FieldDefinition.newDefinition()
                                                 .name("FUGA")
                                                 .position(5, 4)
                                                 .build();
        
        FileDecoder decoder = new FixedFileDecoder(Arrays.asList(hogeDef, fugaDef));
        
        TestFileLineProcessor lineProcessor = new TestFileLineProcessor("HOGE", "FUGA");
        
        try (InputStream in = open(fixed)) {
            FileUploadProcessor processor = new FileUploadProcessor(in, decoder, lineProcessor);
            
            // exercise
            processor.execute();
            
            // verify
            assertThat(lineProcessor.values.get(0), is("hoge"));
            assertThat(lineProcessor.values.get(1), is("fuga"));
            assertThat(lineProcessor.values.get(2), is("aaaa"));
            assertThat(lineProcessor.values.get(3), is("bbbb"));
        }
    }
    
    @Test
    public void 指定した位置のフィールドが抽出されていること() throws Exception {
        // setup
        FieldDefinition fugaDef = FieldDefinition.newDefinition()
                                                .name("FUGA")
                                                .position(2)
                                                .build();
        
        FileDecoder decoder = new CsvFileDecoder(Arrays.asList(fugaDef));
        
        TestFileLineProcessor lineProcessor = new TestFileLineProcessor("FUGA");

        try (InputStream in = open(csv)) {
            FileUploadProcessor processor = new FileUploadProcessor(in, decoder, lineProcessor);
            
            // exercise
            processor.execute();
            
            // verify
            assertThat(lineProcessor.values.get(0), is("fuga1"));
            assertThat(lineProcessor.values.get(1), is("fuga2"));
        }
    }
    
    @Test
    public void 複数の定義を指定した場合も対応できていること() throws Exception {
        // setup
        FieldDefinition hogeDef = FieldDefinition.newDefinition()
                                                 .name("HOGE")
                                                 .position(1)
                                                 .build();

        FieldDefinition fugaDef = FieldDefinition.newDefinition()
                                                 .name("FUGA")
                                                 .position(2)
                                                 .build();
        
        FileDecoder decoder = new CsvFileDecoder(Arrays.asList(hogeDef, fugaDef));
        
        TestFileLineProcessor lineProcessor = new TestFileLineProcessor("HOGE", "FUGA");
        
        try (InputStream in = open(csv)) {
            FileUploadProcessor processor = new FileUploadProcessor(in, decoder, lineProcessor);
            
            // exercise
            processor.execute();
            
            // verify
            assertThat(lineProcessor.values.get(0), is("hoge"));
            assertThat(lineProcessor.values.get(1), is("fuga1"));
            assertThat(lineProcessor.values.get(2), is("aaa"));
            assertThat(lineProcessor.values.get(3), is("fuga2"));
        }
    }
    
    @Test
    public void 数値項目をintで取得できること() throws Exception {
        // setup
        FieldDefinition fugaDef = FieldDefinition.newDefinition()
                                                .name("NUMBER")
                                                .position(4)
                                                .build();
        
        FileDecoder decoder = new CsvFileDecoder(Arrays.asList(fugaDef));
        
        TestNumberFileLineProcessor lineProcessor = new TestNumberFileLineProcessor("NUMBER");

        try (InputStream in = open(csv)) {
            FileUploadProcessor processor = new FileUploadProcessor(in, decoder, lineProcessor);
            
            // exercise
            processor.execute();
            
            // verify
            assertThat(lineProcessor.values.get(0), is(43));
            assertThat(lineProcessor.values.get(1), is(59));
        }
    }
    
    private InputStream open(File file) throws IOException {
        return Files.newInputStream(file.toPath(), StandardOpenOption.READ);
    }
    
    private static class TestFileLineProcessor extends FileLineProcessor {
        
        private List<String> values = new ArrayList<>();
        private String[] names;
        
        public TestFileLineProcessor(String... names) {
            this.names = names;
        }
        
        @Override
        public void process(FileLine fileLine) {
            for (String name : this.names) {
                this.values.add(fileLine.getElement(name));
            }
        }
    }
    
    private static class TestNumberFileLineProcessor extends FileLineProcessor {
        
        private List<Integer> values = new ArrayList<>();
        private String[] names;
        
        public TestNumberFileLineProcessor(String... names) {
            this.names = names;
        }
        
        @Override
        public void process(FileLine fileLine) {
            for (String name : this.names) {
                this.values.add(fileLine.getElementAsInt(name));
            }
        }
    }
}
