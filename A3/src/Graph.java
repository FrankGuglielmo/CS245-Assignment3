import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

public class Graph <T> {

    private int artistCount = 0;
    protected ArrayList<LinkedList<String>> adjacencyList;
    protected Hashtable<String, Integer> artistIndex = new Hashtable<>();


    public void addVertex(String name){
        //Make a new LinkedList to go in the ArrayList
        LinkedList<String> newArtist = new LinkedList<>();
        //Make the first node in the LinkedList start with the name
        newArtist.add(name);
        //Add the newly generated Linked List to the adjacencyList
        adjacencyList.add(newArtist);
        //Make a hashtable to store the indexes of all the artists in the graph
        //for fast access later. Since the value of our vertexes are String and not
        //ints, we need to find a way to keep the fast access int vertexes provide
        //but also retain the value of the string
        artistIndex.put(name, artistCount);
        artistCount++;
    }
    public void addEdge(String artist1, String artist2){
        //Finds the index of the artist in the ArrayList using Hashtable we set
        //values for when we add a new vertex
        LinkedList<String> artist = adjacencyList.get(artistIndex.get(artist1));
        //Adds artist2 to artist1's adjacency LinkedList
        artist.add(artist2);
        //Since the graph is undirected, we also have to add artist1 to artist2's
        //adjacency LinkedList
        //We're assuming both artists have already been added as vertexes in the graph,
        //we'll make sure of this in our main function
        artist = adjacencyList.get(artistIndex.get(artist2));
        artist.add(artist1);
    }

    public LinkedList<String> collaborations(String artist){
        return adjacencyList.get(artistIndex.get(artist));
    }





}

