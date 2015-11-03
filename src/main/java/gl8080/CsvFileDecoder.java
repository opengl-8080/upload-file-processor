package gl8080;

import java.util.List;

public class CsvFileDecoder implements FileDecoder {
    
    private List<FieldDefinition> definitionList;
    
    public CsvFileDecoder(List<FieldDefinition> definitionList) {
        this.definitionList = definitionList;
    }

    @Override
    public FileLine decode(String line) {
        FileLine fileLine = new FileLine();
        String[] elements = line.split(",");
        
        for (FieldDefinition definition : this.definitionList) {
            int position = definition.getPosition().left;
            String element = elements[position - 1];
            
            fileLine.addElement(definition, element);
        }
        
        return fileLine;
    }

}
