package tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by py on 16-10-25.
 */
public class HuffmanTree {
    private TreeNode root;
    private int dep;

    public static void main(String[] args) {
        HuffmanTree tree = new HuffmanTree();
        tree.buildFromFile(Paths.get("Aesop_Fables.txt"));
        System.out.println(tree.toString());
    }

    public void buildFromFile(Path path){
        Map<Character, Integer> charCount = charCountFromFile(path);
        if(charCount == null){
            System.out.println("read data wrong");
            return ;
        }

        System.out.println(charCount.toString());

        // 构造huffman树
        List<TreeNode> nodes = charCount.entrySet().stream()
                .map(entry -> new TreeNode(null, null, entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));

        while(nodes.size() >= 2){
            TreeNode fn = selectAndRemoveSmallestNode(nodes);
            TreeNode sn = selectAndRemoveSmallestNode(nodes);
            nodes.add(new TreeNode(fn, sn, '　', fn.getValue() + sn.getValue()));
        }

        this.root = nodes.get(0);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(this.root);
        int cnt = 1;
        int step = 1;
        while(!queue.isEmpty()){
            TreeNode cn = queue.remove();
            stringBuilder.append("("+cn.getKey()+","+cn.getValue()+") ");
            if(cn.getLeft() != null) queue.add(cn.getLeft());
            if(cn.getRight() != null) queue.add(cn.getRight());
            if(step < cnt){
                step ++;
            }else{
                stringBuilder.append("\n");
                cnt *= 2;
                step = 1;
            }
        }
        return stringBuilder.toString();
    }

    private TreeNode selectAndRemoveSmallestNode(List<TreeNode> nodes){
        int pos = -1;
        for(int i = 0; i < nodes.size() ; ++ i){
            if(pos == -1 || nodes.get(pos).getValue() > nodes.get(i).getValue())
                pos = i;
        }
        TreeNode node = nodes.get(pos);
        nodes.remove(pos);
        return node;
    }
    // 统计文件中每个字符出现的次数
    private Map<Character, Integer> charCountFromFile(Path path){

        if(path == null || !Files.exists(path))
            return null;

        Map<Character, Integer> charCount = new HashMap<>();
        try(Scanner sc = new Scanner(Files.newInputStream(path), "UTF-8")){
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                System.out.println(line);
                for(char c : line.toCharArray()){
                    if(charCount.containsKey(c))
                        charCount.put(c, charCount.get(c) + 1);
                    else charCount.put(c, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return charCount;
    }
}
