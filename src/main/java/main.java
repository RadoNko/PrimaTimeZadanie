import org.javatuples.Triplet;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class main {
    public static void main(String[] args) {
        // 1,
        // 2 -> 3, 4
        // 5 -> 6 -> 7
        List<Node> list = new ArrayList<>();
        list.add(node(1));
        list.add(node(2, new Node[]{ node(3), node(4) }));
        list.add(node(5, new Node[]{ node(6, new Node[]{ node(7) })}));

        // should return 4
        // because (1 + 2 + 3 + 4 + 5 + 6 + 7) / 7 = 4
        getMeanValue(list);
    }

    public interface Node {
        double getValue();
        List<Node> getNodes();
    }
    public static Triplet<Double,Double,Double> inOrderTriplet(Node node, Triplet<Double,Double,Double> triplet,boolean depthMatters) {
        // Triplet 0. - sum
        // Triplet 1. - count
        // Triplet 2. - depth

        // sum += current value
        if (!depthMatters){
            triplet = triplet.setAt0(triplet.getValue0() + node.getValue());
        } else {
            // account for depth in sum
            triplet = triplet.setAt0((triplet.getValue0() + node.getValue()) * Math.pow(0.9,triplet.getValue2()));
        }

        switch (node.getNodes().size()) {
            // has 0 children (is leaf)
            case 0:
                // count ++
                triplet = triplet.setAt1(triplet.getValue1() + 1);
                return triplet;

            // has 1 child
            case 1:
                // count ++
                triplet = triplet.setAt1(triplet.getValue1() + 1);

                // depth ++
                triplet = triplet.setAt2(triplet.getValue2() + 1);

                // sum
                triplet = inOrderTriplet(node.getNodes().get(0),triplet,depthMatters);
                break;

            // has 2 children
            case 2:
                // count ++
                triplet = triplet.setAt1(triplet.getValue1() + 1);

                // depth ++
                triplet = triplet.setAt2(triplet.getValue2() + 1);


                // left sum
                triplet = inOrderTriplet(node.getNodes().get(0),triplet,depthMatters);

                // right sum
                triplet = inOrderTriplet(node.getNodes().get(1),triplet,depthMatters);
                break;
        }
        return triplet;
    }

    public static void getMeanValue(List<Node> nodes) {
        // please implement algorithm for mean value of all given nodes
        // each node has own value and sub-nodes of the same structure,
        // mean value should be calculated across all values in the tree

        // Zadanie mi skôr príde ako pole binárnych stromov keďže niesú prepojenia medzi 1 -> 2 a 3/4 -> 5
        // preto ho tak budeme aj riešiť
        double sum = 0;
        double count = 0;
        for (Node node : nodes) {
            //  node.getValue();
            //System.out.println(inOrderBasic(node,0,0));

            Triplet t = inOrderTriplet(node,Triplet.with(0.0,0.0,0.0),true);
            sum +=  (double) t.getValue0();
            count += (double) t.getValue1();
        }

        // Meranie nieje úplne presne nakoľko Java veľmi zaokrúhluje klasické Double čísla
        // ak by sme chceli pracovať s väčšou presnosťou môžeme použiť napr. BigDecimal

        System.out.println("Mean value with 10% difference in depth value: "+sum/count);

        sum = 0;
        count = 0;
        for (Node node : nodes) {
            Triplet t = inOrderTriplet(node,Triplet.with(0.0,0.0,0.0),false);
            sum +=  (double) t.getValue0();
            count += (double) t.getValue1();
        }
        System.out.println("Mean value basic: "+sum/count);
    }

    // builders

    public static Node node(double value) {
        return node(value, new Node[]{});
    }

    public static Node node(double value, Node[] nodes) {
        return new Node() {
            public double getValue() {
                return value;
            }
            public List<Node> getNodes() {
                return Arrays.asList(nodes);
            }
        };
    }
}