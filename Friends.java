package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

    public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {    
        ArrayList<String> answer=new ArrayList<>();    
        Queue<Person> Que1 = new Queue<Person>();
        Queue<Person> Que2= new Queue<Person>();
        Queue<Queue<Person>> doubleQ=new Queue<Queue<Person>>();

        Que2.enqueue(g.members[g.map.get(p1)]);
        Que1.enqueue(g.members[g.map.get(p1)]);
        doubleQ.enqueue(Que1);

        boolean[] isVisit=new boolean[g.members.length];
        isVisit[g.map.get(p1)]=true;

        while(!Que2.isEmpty()){
            Queue<Person> qCurrent=doubleQ.dequeue();
            for(Friend friend=Que2.dequeue().first; friend != null; friend = friend.next){
                if(isVisit[friend.fnum]==false){
                    isVisit[friend.fnum]=true;
                    Que1=new Queue<Person>();
                    combine(qCurrent,Que1);
                    Que2.enqueue(g.members[friend.fnum]);
                    Que1.enqueue(g.members[friend.fnum]);
                    doubleQ.enqueue(Que1);
                    if(g.members[friend.fnum].name.equals(p2)){
                        while(!Que1.isEmpty()){
                            answer.add(Que1.dequeue().name);
                        }
                        return answer;
                    }
                }
                
            }
        }
        
        return answer;
    }
    
    
    private static void combine(Queue<Person> Queue1, Queue<Person> Queue2){
        for(int cur=Queue1.size();cur>0;cur--){
            Person person=Queue1.dequeue();
            Queue2.enqueue(person);
            Queue1.enqueue(person);
        }
    }

    public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
        boolean[] isVisit=new boolean[g.members.length];
        ArrayList<ArrayList<String>> answer= new ArrayList<ArrayList<String>>();
        
        for(Person person : g.members){
            if(person.student && person.school.equals(school) && !isVisit[g.map.get(person.name)]){
                answer.add(bfs(school, person, isVisit, g));
            }
        }
        return answer;
    }
    
    private static ArrayList<String> bfs(String schoolName, Person person, boolean[] isVisit,Graph g){
        Queue<Person> Queue=new Queue<Person>();
        ArrayList<String> ans=new ArrayList<String>();
        
        isVisit[g.map.get(person.name)]=true;
        Queue.enqueue(person);
        ans.add(person.name);

        while(!Queue.isEmpty()){
            for(Friend friend=Queue.dequeue().first; friend != null; friend=friend.next){
                if((!isVisit[friend.fnum]) &&  g.members[friend.fnum].student && g.members[friend.fnum].school.equals(schoolName)  ){
                    isVisit[friend.fnum]=true;

                    ans.add(ans.size()-1 ,g.members[friend.fnum].name);
                    Queue.enqueue(g.members[friend.fnum]);
                }
            }
        }
        return ans;

    }

    public static ArrayList<String> connectors(Graph g) {
        if(g==null) return null;

        Edge[] edgeOut = new Edge[g.members.length];
    
        ArrayList<String> answer=new ArrayList<>();
        boolean[] isVisit=new boolean[g.members.length];

        for(int cur=0;cur<edgeOut.length;cur++){
            isVisit[cur]=false;
            edgeOut[cur]=new Edge(0,0);
        }

        for(int x=0;x<g.members.length;x++){
            if(isVisit[x]==false){
                dfs(isVisit,answer,x,0,x,edgeOut,1,1,g);
            }
        }
        return answer;
        
    }


    private static void dfs(boolean[] isVisit, ArrayList<String> answer ,int first, int fCount , int x,Edge[] edgeOut, int init, int number ,Graph g){
        
        edgeOut[x].v1=init;
        isVisit[x]=true;
        edgeOut[x].v2=number;
        
        for(Friend friend = g.members[x].first; friend != null ; friend=friend.next){
            int friendNUm=friend.fnum;
            if(isVisit[friend.fnum] == false){
                dfs(isVisit, answer , first,fCount , friend.fnum, edgeOut, init+1, number+1 , g);
                if(first==x){
                    fCount++;
                }
                if(edgeOut[friendNUm].v2 < edgeOut[x].v1){
                    edgeOut[x].v2 = Math.min(edgeOut[friendNUm].v2, edgeOut[x].v2);
                }
                if((edgeOut[x].v1 <= edgeOut[friendNUm].v2) &&(answer.contains(g.members[x].name)==false)  && (x != first || fCount > 1)  ){
                    answer.add(g.members[x].name);
                }
            }
            else{
                edgeOut[x].v2=Math.min(edgeOut[x].v2 , edgeOut[friendNUm].v1);
            }
        }
    } 

    
}