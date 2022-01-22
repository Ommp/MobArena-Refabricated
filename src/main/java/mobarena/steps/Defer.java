package mobarena.steps;

class Defer implements Step {
    private Step step;

    private Defer(Step step) {
        this.step = step;
    }

    @Override
    public void run() {
        // OK BOSS
    }

    @Override
    public void undo() {
        step.run();
    }

    @Override
    public String toString() {
        return "deferred(" + step + ")";
    }

    static StepFactory it(StepFactory factory) {
        return player -> new Defer(factory.create(player));
    }
}