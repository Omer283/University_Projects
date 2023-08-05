function [value] = State(alive,neighbors)
    if alive==1
        if neighbors==2 || neighbors==3
            value=1;
        else
            value=0;
        end
    else
        if neighbors==3
            value=1;
        else
            value=0;
        end
    end
end
