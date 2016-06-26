package com.onekey.bookkeeper;

import java.util.List;
import java.util.Stack;

import com.onekey.bookkeeper.entity.Resource;



public class ResourceStack {
	private static Stack<List<Resource>> resourceListStack;
	private static final ResourceStack instance = new ResourceStack();

    public static ResourceStack create() {
        return instance;
    }
    
    public void add(List<Resource> resourceList) {
    	if (resourceListStack == null) {
    		resourceListStack = new Stack<List<Resource>>();
    	}
    	resourceListStack.add(resourceList);
    }
    
    public List<Resource> top() {
    	if (resourceListStack == null) {
            throw new NullPointerException(
                    "you need add Resource first");
        }
        if (resourceListStack.isEmpty()) {
            return null;
        }
        
        return resourceListStack.lastElement();
    }
    
    public List<Resource> getList(int position) {
    	if (resourceListStack == null) {
            throw new NullPointerException(
                    "you need add Resource first");
        }
        if (resourceListStack.isEmpty()) {
            return null;
        }
        
        return resourceListStack.get(position);
    }
    
    public void removeTop() {
    	List<Resource> resource = top();
        if (resource != null) {
        	resourceListStack.remove(resource);
        	resource = null;
        }
    }
    
    public void remove(List<Resource> resource) {
    	if (resource != null) {
        	resourceListStack.remove(resource);
        	resource = null;
        }
    }
    
    public void removeAll() {
    	for (int i = 0, size = resourceListStack.size(); i < size; i++) {
            if (null != resourceListStack.get(i)) {
            	remove(resourceListStack.get(i));
            }
        }
    	resourceListStack.clear();
    }
	
}
