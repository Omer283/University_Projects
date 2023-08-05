function [state] = findCycleMat(mat)
% finds whether a matrix is a start of a cycle
    stateVec(:,:,1) = mat;
    len = 1;
    while(1) 
       tmp = Turn(stateVec(:,:,len)); %iteration
       for i = 1:len
          if tmp == stateVec(:,:,i)
             state = (i == 1); %if first equals last
             return;
          end
       end
       len = len + 1;
       stateVec(:,:,len) = tmp;
    end
end

