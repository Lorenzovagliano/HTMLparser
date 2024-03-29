import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;
import java.util.EmptyStackException;
import java.io.IOException;

class TreeNode {
    String tag;
    ArrayList<TreeNode> children;
    int depth;

    public TreeNode(String tag) {
        this.tag = tag;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}

class Tree {
    TreeNode root;

    public void setRoot(TreeNode root) {
        this.root = root;
    }
}

public class HtmlAnalyzer {

    public static ArrayList<String> HTMLtoString(String url) throws Exception {
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

        ArrayList<String> stringLines = new ArrayList<>();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            String line = inputLine.trim();
            stringLines.add(line);
        }

        in.close();

        return stringLines;
    }

    public static void printLines(ArrayList<String> lines) {
        for (int i = 0; i < lines.size(); i++){
            System.out.println(lines.get(i));
        }
    }

    public static boolean areMatchingTags(String openingTag, String closingTag) {
        String openingTagName = openingTag.replaceAll("[^a-zA-Z]", "");
        String closingTagName = closingTag.replaceAll("[^a-zA-Z]", "");

        return openingTagName.equals(closingTagName) && openingTag.startsWith("<" + openingTagName) && closingTag.startsWith("</" + closingTagName);
    }

    public static Tree linesToTree(ArrayList<String> lines, boolean[] malformedHTML) {
        Tree tree = new Tree();
        TreeNode currentNode = null;
        Stack<TreeNode> stack = new Stack<>();

        try {
            for (String line : lines) {
                if (line.contains("<")) {
                    if (!line.contains("/")) {
                        TreeNode newNode = new TreeNode(line);

                        if (!stack.isEmpty()) {
                            TreeNode parentNode = stack.peek();
                            parentNode.addChild(newNode);
                        }
                        stack.push(newNode);

                        if (tree.root == null) {
                            tree.setRoot(newNode);
                        }

                        currentNode = newNode;

                    } else if (line.contains("/")) {
                        if(!areMatchingTags(stack.peek().tag, line)){
                            malformedHTML[0] = true;
                        }
                        stack.pop();

                        if (!stack.isEmpty()) {
                            currentNode = stack.peek();
                        } else {
                            currentNode = null;
                        }
                    }
                } else {
                    if (currentNode != null) {
                        currentNode.addChild(new TreeNode(line));
                    }
                }
            }
        } catch (EmptyStackException e) {
            malformedHTML[0] = true;
        }

        if(!stack.isEmpty()){
            malformedHTML[0] = true;
        }

        return tree;
    }


    private static void printTree(TreeNode node, int level, ArrayList<String> text) {
        if (node != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < level; i++) {
                sb.append("\t");
            }
            text.add(sb.toString() + node.tag);
            for (TreeNode child : node.children) {
                printTree(child, level + 1, text);
            }
        }
    }

    public static String findDeepestNode(ArrayList<String> text) {
        int maxIndentation = -1;
        String deepestNode = null;

        for (String line : text) {
            int indentation = 0;
            while (line.charAt(indentation) == '\t') {
                indentation++;
            }

            if (indentation > maxIndentation) {
                maxIndentation = indentation;
                deepestNode = line.trim();
            }
        }

        return deepestNode;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HtmlAnalyzer <URL>");
            return;
        }

        String url = args[0];

        try {
            ArrayList<String> lines = HTMLtoString(url);

            boolean[] malformedHTML = { false };
            Tree tree = linesToTree(lines, malformedHTML);

            if (malformedHTML[0]) {
                System.out.println("malformed HTML");
            } else {
                ArrayList<String> text = new ArrayList<>();
                printTree(tree.root, 0, text);
                System.out.println(findDeepestNode(text));
            }
        } catch (IOException e) {
            System.out.println("URL connection error");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
