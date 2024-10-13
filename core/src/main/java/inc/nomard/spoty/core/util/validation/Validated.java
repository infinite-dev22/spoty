package inc.nomard.spoty.core.util.validation;

import javafx.css.PseudoClass;
import javafx.scene.Node;

import java.util.List;

public interface Validated {
    PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    Validator getValidator();

    default boolean isValid() {
        return this.getValidator() != null && this.getValidator().isValid();
    }

    default List<Constraint> validate() {
        return this.getValidator() != null ? this.getValidator().validate() : List.of();
    }

    default void updateInvalid(Node node, boolean invalid) {
        node.pseudoClassStateChanged(PseudoClass.getPseudoClass("invalid"), invalid);
    }
}