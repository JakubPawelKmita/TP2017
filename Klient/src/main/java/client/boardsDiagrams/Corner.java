package client.boardsDiagrams;

/**
 * Created by Jakub Kmita on 03.01.2018.
 */
public enum Corner {
    firstCorner, secondCorner, thirdCorner, fourthCorner, fifthCorner, sixthCorner;

    public Color getColor(){
        switch(this){
            case firstCorner: return Color.blue;
            case secondCorner: return Color.yellow;
            case thirdCorner: return Color.red;
            case fourthCorner: return Color.green;
            case fifthCorner: return Color.orange;
            case sixthCorner: return Color.purple;
            default: return null;
        }
    }

    public byte[] getCoordinates(){
        switch(this){
            case firstCorner: return CornersCoordinates.firstCorner;
            case secondCorner: return CornersCoordinates.secondCorner;
            case thirdCorner: return CornersCoordinates.thirdCorner;
            case fourthCorner: return CornersCoordinates.fourthCorner;
            case fifthCorner: return CornersCoordinates.fifthCorner;
            case sixthCorner: return CornersCoordinates.sixthCorner;
            default: return null;
        }
    }
}
