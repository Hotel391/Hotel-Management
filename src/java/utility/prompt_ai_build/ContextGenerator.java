package utility.prompt_ai_build;

/**
 *
 * @author HieuTT
 */
public class ContextGenerator {
    private RolePromptStrategy strategy;

    public void setStrategy(RolePromptStrategy strategy){
        this.strategy= strategy;
    }

    public String generateContext(){
        if(strategy == null) {
            return "No strategy set";
        }
        return strategy.generatePrompt();
    }
}
