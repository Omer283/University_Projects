function [stateVec] = searchPeriods(sizeVec)
%searchPeriods looks for periodic starting states within given size,
%returns number array (and not matrix array!)
stateVec = [];
for i = 0:(2^(sizeVec(1)*sizeVec(2))-1) %runs for all possible starting states
    if (findCycleMat(numToMat(i,sizeVec))) 
        stateVec(end+1) = i; 
    end
end
end

