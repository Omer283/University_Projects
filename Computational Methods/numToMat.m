function [matVec] = numToMat(numVec, sizeVec)
%numToMat converts vector of numbers into their GOL representations
%   requires size of matrices
len = length(numVec); 
matVec = zeros(sizeVec(1),sizeVec(2),len); %preallocation
for i = 1:len
    matVec(:,:,i)=vec2mat(de2bi(numVec(i),sizeVec(1)*sizeVec(2)),sizeVec(2));
    %converts each number to binary vector, and then converts the vector
    %into a matrix in appropriate size
end
end

