package in.tech_camp.prototype_d.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({ ValidationPriority1.class, ValidationPriority2.class})
public interface ValidationOrder {
  
}
