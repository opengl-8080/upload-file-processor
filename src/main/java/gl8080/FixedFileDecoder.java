package gl8080;

import java.util.List;

public class FixedFileDecoder implements FileDecoder {
    
    private List<FieldDefinition> definitionList;
    
    public FixedFileDecoder(List<FieldDefinition> definitionList) {
        this.definitionList = definitionList;
    }

    @Override
    public FileLine decode(String line) {
        System.out.println(line);
        FileLine fileLine = new FileLine();
        
        for (FieldDefinition definition : this.definitionList) {
            int beginIndex = definition.getPosition().left - 1;
            int endIndex = beginIndex + definition.getPosition().length;
            
            String element = line.substring(beginIndex, endIndex);
            
            fileLine.addElement(definition, element);
        }
        
        return fileLine;
    }
}
