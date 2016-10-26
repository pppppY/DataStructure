package tree;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Created by py on 16-10-25.
 */
public class HuffmanTree {
    // 树根
    private TreeNode root;
    // 字符->编码映射
    private Map<Character, Boolean[]> char2Code;

    public static void main(String[] args) {
        HuffmanTree tree = new HuffmanTree();
        tree.buildFromFile(Paths.get("Aesop_Fables.txt"));
        System.out.println(tree.computeCompressionRatio(Paths.get("Aesop_Fables.txt")));
    }
    // 假设每个字符占一字节，计算压缩比
    public double computeCompressionRatio(Path path){
        Map<Character, Integer> charCount = this.charCountFromFile(path);
        int charNum = charCount.values().stream().reduce(0, Integer::sum);
        return 1 - this.encode(path).length * 1.0d  / (charNum * 8);
    }
    // 根据path对应的文件中的数据建立huffman树
    public void buildFromFile(Path path){

        Map<Character, Integer> charCount = charCountFromFile(path);
        if(charCount == null){
            System.out.println("read data wrong");
            return ;
        }

        build(charCount);

        buildChar2Code();

    }
    // 对一个文件进行编码
    public Boolean[] encode(Path path){
        if(path == null || !Files.exists(path))
            return null;
        List<Boolean> code = new ArrayList<>();
        try(InputStreamReader reader = new InputStreamReader(Files.newInputStream(path), "UTF-8")){

            while(reader.ready()){
                char c = (char) reader.read();
                code.addAll(Arrays.asList(encode(c)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code.stream().toArray(Boolean[]::new);
    }

    // 对一段文本进行编码
    public Boolean[] encode(String str){
        List<Boolean> code = new ArrayList<>();
        for(char c : str.toCharArray()){
            if(this.char2Code.containsKey(c))
                code.addAll(Arrays.asList(this.char2Code.get(c)));
            else
                System.out.println("-----------> " + c +" not exits");
        }
        return code.stream().toArray(Boolean[]::new);
    }
    public Boolean[] encode(char c){
        return this.char2Code.get(c);
    }
    // 解码
    public String decode(Boolean[] code){
        StringBuilder str = new StringBuilder();
        TreeNode cNode = this.root;
        for(int idx = 0 ; idx < code.length; ){
            while(cNode.getLeft() != null || cNode.getRight() != null){
                if(code[idx] == Boolean.TRUE && cNode.getRight() != null)
                    cNode = cNode.getRight();
                else if(code[idx] == Boolean.FALSE && cNode.getLeft() != null)
                    cNode = cNode.getLeft();
                else {
                    System.out.println("wrong code!");
                    return null;
                }
                idx ++ ;
            }
            str.append(cNode.getKey());
            cNode = this.root;
        }
        return str.toString();
    }
    // charCount: Map, 每个字符出现的个数统计
    private void build( Map<Character, Integer> charCount){

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

    // 建立字符->编码间的映射char2Code
    private void buildChar2Code(){
        char2Code = new HashMap<>();
        List<Boolean> code = new LinkedList<>();
        buildChar2CodeRc(this.root, code);

    }
    // 递归构造每一个字符的编码，存储到Map char2Code中
    private void buildChar2CodeRc(TreeNode cnode, List<Boolean> code){
        if(cnode.getLeft() == null && cnode.getRight() == null){
            Boolean[] lcode = code.stream().toArray(Boolean[]::new);
            this.char2Code.put(cnode.getKey(), lcode);
        }else{
            code.add(false);
            buildChar2CodeRc(cnode.getLeft(), code);
            code.set(code.size() - 1, true);
            buildChar2CodeRc(cnode.getRight(), code);
            code.remove(code.size() - 1);
        }
    }
    // 返回nodes中value最小的节点，并在nodes中删除该节点
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
        try(InputStreamReader reader = new InputStreamReader(Files.newInputStream(path), "UTF-8")){
            while(reader.ready()){
                char c = (char) reader.read();
                if(charCount.containsKey(c))
                    charCount.put(c, charCount.get(c) + 1);
                else charCount.put(c, 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return charCount;
    }
}
