package jgrapht.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CapacityVertex {
    private String name;
    private int capacity;
    
    public CapacityVertex(String name) {
        this(name, 1);
    }
}
