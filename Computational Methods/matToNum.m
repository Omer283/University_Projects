function [numVec] = matToNum(matVec)
%matToNum converts matrice(s) to regular numbers
%   matVec is a 3 dimensional matrix (third dimension
%   refers to the matrix number, first 2 to their entries)
sizeVec = size(matVec);
if (length(sizeVec) == 2) % if only 1 matrix was sent
   sizeVec(3) = 1; 
end
binaryMat = zeros(sizeVec(3),sizeVec(1)*sizeVec(2)); %preallocating
for i = 1:sizeVec(3)
   binaryMat(i,:) =  reshape(transpose(matVec(:,:,i)),[1,sizeVec(1)*sizeVec(2)]); 
   %converts array of matrices into an array of row vectors (2 dimensional
   %matrix)
end
numVec = bi2de(binaryMat); %converts binary to decimal
end

