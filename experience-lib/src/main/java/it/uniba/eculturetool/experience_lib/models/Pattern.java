package it.uniba.eculturetool.experience_lib.models;

public class Pattern extends Experience {
    public static final int NUM_DOT = 3;

    private final int[][] matrixPattern = new int[NUM_DOT][NUM_DOT];    // Matrice di zeri

    public Pattern(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public Pattern() {
        super();
    }

    public int[][] getMatrixPattern() {
        return matrixPattern;
    }

    public void setMatrixPattern(int[][] matrixPattern) {
        if(matrixPattern.length != NUM_DOT && matrixPattern[0].length != NUM_DOT)
            throw new IllegalArgumentException("The matrix should be " + NUM_DOT + "x" + NUM_DOT + ". You provided a " + matrixPattern.length + "x" + matrixPattern[0].length);

        for(int i=0; i<matrixPattern.length; i++) {
            System.arraycopy(matrixPattern[i], 0, this.matrixPattern[i], 0, matrixPattern[i].length);
        }
    }

    public boolean isMatrixEmpty() {
        for (int[] rows : matrixPattern) {
            for (int element : rows) {
                if (element != 0) return true;
            }
        }

        return false;
    }
}
