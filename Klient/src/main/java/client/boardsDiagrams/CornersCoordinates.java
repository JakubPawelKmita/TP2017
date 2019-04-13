package client.boardsDiagrams;

/**
 * Created by Jakub Kmita on 02.01.2018.
 */
class CornersCoordinates {

    /*
    Corner coordinates are formed in way, where every next two numbers (starting at even positions) creates one coordinate
    ex.: [10,20, 30,40] represent two points: (row:10, column:20) and (row:30, column:40)
     */
    static byte[] firstCorner = {
            0,12,
            1,11, 1,12,
            2,10, 2,11, 2,12,
            3,9, 3,10, 3,11, 3,12
    };

    static byte[] secondCorner = {
            4,13, 4,14, 4,15, 4,16,
            5,13, 5,14, 5,15,
            6,13, 6,14,
            7,13
    };

    static byte[] thirdCorner = {
            9,12,
            10,11, 10,12,
            11,10, 11,11, 11,12,
            12,9, 12,10, 12,11, 12,12
    };

    static byte[] fourthCorner = {
            13,4, 13,5, 13,6, 13,7,
            14,4, 14,5, 14,6,
            15,4, 15,5,
            16,4
    };

    static byte[] fifthCorner = {
            9,3,
            10,2, 10,3,
            11,1, 11,2, 11,3,
            12,0, 12,1, 12,2, 12,3
    };

    static byte[] sixthCorner = {
            4,4, 4,5, 4,6, 4,7,
            5,4, 5,5, 5,6,
            6,4, 6,5,
            7,4
    };
}
